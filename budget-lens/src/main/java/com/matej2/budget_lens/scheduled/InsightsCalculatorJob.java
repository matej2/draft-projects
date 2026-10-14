package com.matej2.budget_lens.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matej2.budget_lens.domain.dto.request.ExpenseFilterRequest;
import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.dto.response.CategoryResponse;
import com.matej2.budget_lens.domain.entity.Expense;
import com.matej2.budget_lens.domain.entity.RecordLimit;
import com.matej2.budget_lens.domain.integration.AiCategorizationRequest;
import com.matej2.budget_lens.domain.mapper.ExpenseMapper;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import com.matej2.budget_lens.repository.domain.RecordLimiter;
import com.matej2.budget_lens.service.domain.CategoryService;
import com.matej2.budget_lens.service.domain.InsightCalculatorService;
import com.matej2.budget_lens.service.domain.InsightService;
import com.matej2.budget_lens.service.integration.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static com.matej2.budget_lens.utils.DateUtils.getFirstDayOfTheMonth;
import static com.matej2.budget_lens.utils.DateUtils.getLastDayOfTheMonth;

@Component
@RequiredArgsConstructor
public class InsightsCalculatorJob {
    private final CategoryService categoryService;
    private final InsightService insightService;
    private final ExpenseRepository expenseRepository;
    private final RecordLimiter recordLimiter;
    private final InsightCalculatorService insightCalculatorService;
    private final ChatGPTService chatService;
    private final ExpenseMapper expenseMapper;
    private final ObjectMapper objectMapper;

    private void saveInsights(List<CategoryInsightsResponse> insightList, YearMonth yearMonth) {
        insightService.saveInsightList(insightList, yearMonth);
    }

    private List<Calendar> generateCalendarInstancesForLastTreeMonths() {
        List<Calendar> result = new ArrayList<>();
        Calendar calendarMonth = Calendar.getInstance();

        java.util.stream.IntStream
                .rangeClosed(0, 6)
                .forEach(
                i -> {
                    calendarMonth.add(Calendar.MONTH, -i);
                    result.add((Calendar)calendarMonth.clone());
                    calendarMonth.setTime(new Date());
                }
        );

        return result;
    }

    @Scheduled(fixedDelayString = "1m")
    public void calculateInsights() {
        List<Calendar> treeMonths = generateCalendarInstancesForLastTreeMonths();

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

            saveInsights(categoryInsightsResponseList, yearMonth);
        });
    }

    private AiCategorizationRequest mapToRequest(Expense expense) {
        return new AiCategorizationRequest(
                expense.getId(),
                expense.getNote(),
                expense.getSource(),
                expense.getExpenseDate()
        ) ;
    }

    private void processBatch() throws JsonProcessingException {
        Page<Expense> expensePage;
        do {
            Pageable sortedByDate = PageRequest.of(
                    0,
                    30);
            expensePage = this.expenseRepository.findAll(sortedByDate);

            List<Expense> expenseDataSlice = expensePage.getContent();
            List<AiCategorizationRequest> mappedExpenseList = expenseDataSlice.stream()
                    .map(this::mapToRequest)
                    .toList();

            chatService.getResponse(mappedExpenseList);
        } while(expensePage.hasNext());
    }

    @Scheduled(fixedDelayString = "1h")
    public void categorizeInsights() {
        List<Calendar> treeMonths = generateCalendarInstancesForLastTreeMonths();
        for (Calendar calendar : treeMonths) {

        }
    }

    @Scheduled(fixedDelayString = "15m")
    public void calculateRecordLimits() {
        long expenseCount = expenseRepository.count();
        RecordLimit recordLimit = recordLimiter.findOneByClassName("Expense");
        recordLimit.setCurrentCount(expenseCount);
        recordLimiter.save(recordLimit);
    }
}
