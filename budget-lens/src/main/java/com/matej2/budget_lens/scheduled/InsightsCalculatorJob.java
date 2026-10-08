package com.matej2.budget_lens.scheduled;

import com.matej2.budget_lens.domain.dto.request.ExpenseFilterRequest;
import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.dto.response.CategoryResponse;
import com.matej2.budget_lens.domain.entity.RecordLimit;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import com.matej2.budget_lens.repository.domain.RecordLimiter;
import com.matej2.budget_lens.service.domain.CategoryService;
import com.matej2.budget_lens.service.domain.InsightCalculatorService;
import com.matej2.budget_lens.service.domain.InsightService;
import lombok.RequiredArgsConstructor;
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

    @Scheduled(fixedDelayString = "15m")
    public void calculateRecordLimits() {
        long expenseCount = expenseRepository.count();
        RecordLimit recordLimit = recordLimiter.findOneByClassName("Expense");
        recordLimit.setCurrentCount(expenseCount);
        recordLimiter.save(recordLimit);
    }
}
