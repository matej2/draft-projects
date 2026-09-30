package com.matej2.budget_lens.scheduled;

import com.matej2.budget_lens.domain.dto.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.dto.CategoryResponse;
import com.matej2.budget_lens.domain.dto.ExpenseFilterRequest;
import com.matej2.budget_lens.domain.entity.RecordLimit;
import com.matej2.budget_lens.domain.jpa.CategoryInsightResultRow;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import com.matej2.budget_lens.repository.domain.RecordLimiter;
import com.matej2.budget_lens.service.domain.CategoryService;
import com.matej2.budget_lens.service.domain.ExpenseTrackingService;
import com.matej2.budget_lens.service.domain.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InsightsCalculatorJob {
    private final ExpenseTrackingService expenseTrackingService;
    private final CategoryService categoryService;
    private final InsightService insightService;
    private final ExpenseRepository expenseRepository;
    private final RecordLimiter recordLimiter;


    private LocalDate convertToLocalDate(Date dateToConvert) {
        return LocalDate.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    private CategoryInsightsResponse calculateInsightDetails(CategoryResponse category, ExpenseFilterRequest expenseRequest, YearMonth yearMonth) {
        CategoryInsightResultRow insightResponse = expenseTrackingService.insightsByCategory(expenseRequest, category.id());

        if (insightResponse == null) {
            return new CategoryInsightsResponse(
                    category.id(),
                    yearMonth,
                    category,
                    0f,
                    0f,
                    0f
            );
        } else {
            return new CategoryInsightsResponse(
                    category.id(),
                    yearMonth,
                    category,
                    insightResponse.stdDev(),
                    insightResponse.stdDevPercent(),
                    insightResponse.avg()
            );
        }
    }

    private LocalDate getFirstDayOfTheMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDateOfPreviousMonth = calendar.getTime();

        return convertToLocalDate(firstDateOfPreviousMonth);
    }

    private LocalDate getLastDayOfTheMonth(Calendar calendar) {
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDateOfPreviousMonth = calendar.getTime();

        return convertToLocalDate(lastDateOfPreviousMonth);
    }

    private void saveInsights(List<CategoryInsightsResponse> insightList, YearMonth yearMonth) {
        insightService.saveInsightList(insightList, yearMonth);
    }

    private List<Calendar> generateCalendarInstancesForLastTreeMonths() {
        List<Calendar> result = new ArrayList<>();
        Calendar calendarMonth = Calendar.getInstance();

        java.util.stream.IntStream
                .rangeClosed(0, 2)
                .forEach(
                i -> {
                    calendarMonth.add(Calendar.MONTH, -i);
                    result.add(calendarMonth);
                    calendarMonth.setTime(new Date());
                }
        );

        return result;
    }

    @Scheduled(fixedDelayString = "1m")
    public void calculateInsights() {
        List<Calendar> treeMonths = generateCalendarInstancesForLastTreeMonths();

        treeMonths.forEach(c -> {
            LocalDate firstLocalDate = getFirstDayOfTheMonth(c);
            LocalDate lastLocalDate = getLastDayOfTheMonth(c);
            YearMonth yearMonth = YearMonth.from(lastLocalDate);

            ExpenseFilterRequest avgExpenseRequest = new ExpenseFilterRequest(firstLocalDate, lastLocalDate);
            List<CategoryInsightsResponse> categoryInsightsResponseList = new ArrayList<>();

            List<CategoryResponse> categoryList = categoryService.getAllCategories();
            categoryList.forEach(category -> categoryInsightsResponseList.add(
                    calculateInsightDetails(category, avgExpenseRequest, yearMonth)
            ));

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
