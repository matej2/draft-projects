package com.example.expense_tracker.job;

import com.example.expense_tracker.domain.dto.CategoryInsightsResponse;
import com.example.expense_tracker.domain.dto.CategoryResponse;
import com.example.expense_tracker.domain.dto.ExpenseFilterRequest;
import com.example.expense_tracker.service.CategoryService;
import com.example.expense_tracker.service.ExpenseTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    private LocalDate convertToLocalDate(Date dateToConvert) {
        return LocalDate.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    private CategoryInsightsResponse calculateInsightDetails(CategoryResponse category, ExpenseFilterRequest expenseRequest) {
        Float averageCostLastMonth =  expenseTrackingService.averageCostByCategory(expenseRequest, category.id());
        averageCostLastMonth = averageCostLastMonth != null ? averageCostLastMonth : 0f;

        return new CategoryInsightsResponse(
                category.name(),
                averageCostLastMonth,
                0f,
                0f
        );
    }

    private LocalDate getFirstDayOfThePreviousMonth() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDateOfPreviousMonth = calendar.getTime();

        return convertToLocalDate(firstDateOfPreviousMonth);
    }

    private LocalDate getLastDayOfThePreviousMonth() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDateOfPreviousMonth = calendar.getTime();

        return convertToLocalDate(lastDateOfPreviousMonth);
    }

    private void saveInsights(List<CategoryInsightsResponse> insightList) {
        System.out.println("Saving insights for current month");
        System.out.println(insightList);
    }

    @Scheduled(fixedDelayString = "5m")
    public void calculateInsights() {
        LocalDate firstLocalDate = getFirstDayOfThePreviousMonth();
        LocalDate lastLocalDate = getLastDayOfThePreviousMonth();
        ExpenseFilterRequest avgExpenseRequest = new ExpenseFilterRequest(firstLocalDate, lastLocalDate);
        List<CategoryInsightsResponse> categoryInsightsResponseList = new ArrayList<>();

        List<CategoryResponse> categoryList = categoryService.getAllCategories();
        categoryList.forEach(category -> {
            categoryInsightsResponseList.add(
                    calculateInsightDetails(category, avgExpenseRequest)
            );
        });

        saveInsights(categoryInsightsResponseList);

        System.out.println("InsightsCalculatorJob ended");
    }
}
