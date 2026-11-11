package com.matej2.budget_lens.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.matej2.budget_lens.mapper.AICategorizationMapper;
import com.matej2.budget_lens.model.entity.Expense;
import com.matej2.budget_lens.model.entity.RecordLimit;
import com.matej2.budget_lens.model.integration.AiCategorizationRequest;
import com.matej2.budget_lens.model.integration.AiCategorizationResponse;
import com.matej2.budget_lens.model.request.ExpenseFilterRequest;
import com.matej2.budget_lens.model.response.CategoryInsightsResponse;
import com.matej2.budget_lens.model.response.CategoryResponse;
import com.matej2.budget_lens.repository.RecordLimiter;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import com.matej2.budget_lens.service.domain.CategoryService;
import com.matej2.budget_lens.service.domain.ExpenseTrackingService;
import com.matej2.budget_lens.service.domain.InsightCalculatorService;
import com.matej2.budget_lens.service.domain.InsightService;
import com.matej2.budget_lens.service.integration.GeminiService;
import com.matej2.budget_lens.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.matej2.budget_lens.utils.DateUtils.getFirstDayOfTheMonth;
import static com.matej2.budget_lens.utils.DateUtils.getLastDayOfTheMonth;

@Component
@RequiredArgsConstructor
public class InsightsCalculatorJob {
    private final CategoryService categoryService;
    private final InsightService insightService;
    private final ExpenseTrackingService expenseService;
    private final ExpenseRepository expenseRepository;
    private final RecordLimiter recordLimiter;
    private final InsightCalculatorService insightCalculatorService;
    private final AICategorizationMapper aicategorizationMapper;
    private final GeminiService geminiService;

    @Value("${spring.ai.enabled}")
    private boolean isAiEnabled;

    @Scheduled(fixedDelayString = "15m")
    public void calculateInsights() {
        List<Calendar> treeMonths = DateUtils.generateCalendarInstancesForLastTreeMonths();

        treeMonths.forEach(month -> {
            LocalDate firstLocalDate = getFirstDayOfTheMonth(month);
            LocalDate lastLocalDate = getLastDayOfTheMonth(month);
            YearMonth yearMonth = YearMonth.from(lastLocalDate);

            ExpenseFilterRequest avgExpenseRequest = new ExpenseFilterRequest(firstLocalDate, lastLocalDate);

            List<CategoryResponse> categoryList = categoryService.getAllCategories();

            List<CategoryInsightsResponse> categoryInsightsResponseList = categoryList.stream()
                    .map(category -> insightCalculatorService.calculateInsightDetails(category, avgExpenseRequest, yearMonth))
                    .filter(Objects::nonNull)
                    .toList();

            insightService.saveInsightList(categoryInsightsResponseList, yearMonth);
        });
    }

    private List<AiCategorizationResponse> getExpenseCategorizationForDateRange(LocalDate firstLocalDate, LocalDate lastLocalDate) throws JsonProcessingException {
        Page<Expense> expensePage;
        List<AiCategorizationResponse> categorizationResponses = new ArrayList<>();

        do {
            Pageable sortedByDate = PageRequest.of(
                    0,
                    30);
            expensePage = this.expenseRepository.findByExpenseDateBetween(firstLocalDate, lastLocalDate, sortedByDate);

            if (expensePage.getContent().isEmpty()) {
                return List.of();
            }

            List<AiCategorizationRequest> mappedExpenseList =  expensePage.getContent().stream()
                    .map(aicategorizationMapper::mapToRequest)
                    .toList();

            List<AiCategorizationResponse> aiResponses = geminiService.getAnswer(mappedExpenseList);
            categorizationResponses.addAll(aiResponses);

        } while(expensePage.hasNext());

        return categorizationResponses;
    }

    private void saveCategorization(List<AiCategorizationResponse> categorizationResponses) {
        expenseRepository.saveAll(
                categorizationResponses
                        .stream()
                        .distinct()
                        .filter(res -> res.expenseId() != null || res.categoryId() != null)
                        .map(this.expenseService::applyAiCategorization)
                        .toList()
        );
    }

    @Scheduled(fixedDelayString = "1h")
    public void categorizeInsights() throws JsonProcessingException {
        if (!isAiEnabled) {
            return;
        }
        List<Calendar> treeMonths = DateUtils.generateCalendarInstancesForLastTreeMonths();
        List<AiCategorizationResponse> categorizationResponses = new ArrayList<>(List.of());

        for (Calendar calendar : treeMonths) {
            LocalDate firstLocalDate = getFirstDayOfTheMonth(calendar);
            LocalDate lastLocalDate = getLastDayOfTheMonth(calendar);
            categorizationResponses.addAll(getExpenseCategorizationForDateRange(firstLocalDate, lastLocalDate));
        }
        saveCategorization(categorizationResponses);

    }

    @Scheduled(fixedDelayString = "15m")
    public void calculateRecordLimits() {
        long expenseCount = expenseRepository.count();
        RecordLimit recordLimit = recordLimiter.findOneByClassName("Expense");
        recordLimit.setCurrentCount(expenseCount);
        recordLimiter.save(recordLimit);
    }
}
