package com.example.expense_tracker.job;

import com.example.expense_tracker.domain.dto.CategoryInsightsResponse;
import com.example.expense_tracker.domain.dto.CategoryResponse;
import com.example.expense_tracker.domain.dto.ExpenseFilterRequest;
import com.example.expense_tracker.domain.jpa.CategoryInsightResultRow;
import com.example.expense_tracker.service.CategoryService;
import com.example.expense_tracker.service.ExpenseTrackingService;
import com.example.expense_tracker.service.InsightService;
import com.example.expense_tracker.service.SiStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    private final InsightService insightService;
    private final SiStatService siStatService;


    private LocalDate convertToLocalDate(Date dateToConvert) {
        return LocalDate.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    private CategoryInsightsResponse calculateInsightDetails(CategoryResponse category, ExpenseFilterRequest expenseRequest) {
        CategoryInsightResultRow insightResponse = expenseTrackingService.insightsByCategory(expenseRequest, category.id());

        if (insightResponse == null) {
            return new CategoryInsightsResponse(
                    category.name(),
                    0f,
                    0f,
                    0f
            );
        } else {
            return new CategoryInsightsResponse(
                    category.name(),
                    insightResponse.stdDev(),
                    insightResponse.stdDevPercent(),
                    insightResponse.avg()
            );
        }
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
        insightService.saveInsightList(insightList);
    }

    @Scheduled(fixedDelayString = "5m")
    public void calculateInsights() throws IOException {
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

        siStatService.getAvgYearlyInflation();

        saveInsights(categoryInsightsResponseList);
    }
}
