package com.matej2.budget_lens.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.matej2.budget_lens.domain.dto.request.ExpenseFilterRequest;
import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.dto.response.CategoryResponse;
import com.matej2.budget_lens.domain.dto.response.ExpenseResponse;
import com.matej2.budget_lens.domain.entity.Expense;
import com.matej2.budget_lens.domain.entity.RecordLimit;
import com.matej2.budget_lens.domain.integration.AiCategorizationRequest;
import com.matej2.budget_lens.domain.integration.AiCategorizationResponse;
import com.matej2.budget_lens.domain.mapper.AICategorizationMapper;
import com.matej2.budget_lens.domain.mapper.ExpenseMapper;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import com.matej2.budget_lens.repository.domain.RecordLimiter;
import com.matej2.budget_lens.service.domain.CategoryService;
import com.matej2.budget_lens.service.domain.ExpenseTrackingService;
import com.matej2.budget_lens.service.domain.InsightCalculatorService;
import com.matej2.budget_lens.service.domain.InsightService;
import com.matej2.budget_lens.service.integration.ChatGPTService;
import com.matej2.budget_lens.utils.DateUtils;
import lombok.RequiredArgsConstructor;
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
    private final ChatGPTService chatService;
    private final AICategorizationMapper aicategorizationMapper;
    private final ExpenseMapper expenseMapper;

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

    private List<AiCategorizationResponse> processBatchForDateRange(LocalDate firstLocalDate, LocalDate lastLocalDate) throws JsonProcessingException {
        Page<Expense> expensePage;
        List<AiCategorizationResponse> categorizationResponses = new ArrayList<>();

        do {
            Pageable sortedByDate = PageRequest.of(
                    0,
                    30);
            expensePage = this.expenseRepository.findByExpenseDateBetween(firstLocalDate, lastLocalDate, sortedByDate);

            List<AiCategorizationRequest> mappedExpenseList =  expensePage.getContent().stream()
                    .map(aicategorizationMapper::mapToRequest)
                    .toList();

            categorizationResponses.add(chatService.getResponse(mappedExpenseList));
        } while(expensePage.hasNext());

        return categorizationResponses;
    }

    public ExpenseResponse buildExpenseRequest(AiCategorizationResponse expenseRequest) {
        CategoryResponse category = categoryService.getCategory(expenseRequest.categoryId());
        ExpenseResponse existingExpense = expenseService.getExpenseById(expenseRequest.expenseId());

        return existingExpense.withCategory(category.id());
    }

    private void saveCategorization(List<AiCategorizationResponse> categorizationResponses) {
        categorizationResponses.forEach(categorizationResponse -> {
            ExpenseResponse newExpense = buildExpenseRequest(categorizationResponse);
            expenseService.addExpense(expenseMapper.toRequest(newExpense));
        });
    }

    @Scheduled(fixedDelayString = "3h")
    public void categorizeInsights() throws JsonProcessingException {
        List<Calendar> treeMonths = DateUtils.generateCalendarInstancesForLastTreeMonths();
        for (Calendar calendar : treeMonths) {
            LocalDate firstLocalDate = getFirstDayOfTheMonth(calendar);
            LocalDate lastLocalDate = getLastDayOfTheMonth(calendar);
            List<AiCategorizationResponse> categorizationResponses = processBatchForDateRange(firstLocalDate, lastLocalDate);
            saveCategorization(categorizationResponses);
        }
    }

    @Scheduled(fixedDelayString = "1h")
    public void calculateRecordLimits() {
        long expenseCount = expenseRepository.count();
        RecordLimit recordLimit = recordLimiter.findOneByClassName("Expense");
        recordLimit.setCurrentCount(expenseCount);
        recordLimiter.save(recordLimit);
    }
}
