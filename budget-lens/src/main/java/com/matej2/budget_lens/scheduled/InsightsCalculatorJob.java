package com.matej2.budget_lens.scheduled;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.domain.dto.request.ExpenseFilterRequest;
import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.dto.response.CategoryResponse;
import com.matej2.budget_lens.domain.dto.response.ExpenseResponse;
import com.matej2.budget_lens.domain.entity.RecordLimit;
import com.matej2.budget_lens.domain.jpa.CategoryInsightResultRow;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import com.matej2.budget_lens.repository.domain.RecordLimiter;
import com.matej2.budget_lens.service.domain.BudgetService;
import com.matej2.budget_lens.service.domain.CategoryService;
import com.matej2.budget_lens.service.domain.ExpenseTrackingService;
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
    private final ExpenseTrackingService expenseTrackingService;
    private final CategoryService categoryService;
    private final InsightService insightService;
    private final ExpenseRepository expenseRepository;
    private final RecordLimiter recordLimiter;
    private final BudgetService budgetService;

    private double calculateSafeBudgetConfidence(CategoryInsightResultRow insightResponse) {
        // Safe budget confidence calculation
        double expenseCountRelativeToOptimal;

        if (insightResponse.getExpenseCount() > 2 && insightResponse.getExpenseCount() < 30) {
            // Optimal expense count is 30. Confidence is calculated as % towards that count.
            expenseCountRelativeToOptimal = insightResponse.getExpenseCount().floatValue() / 28.0;
        } else {
            return 0d;
        }

        // Optimal expense standard deviation is 0 %. Confidence is calculated as % towards that number.
        double expenseStandardDeviationRelativeToOptimal = (1.0 - insightResponse.getStdDevPercent());

        // Calculations are combined using ratio 70:30
        return expenseCountRelativeToOptimal * 0.7 + expenseStandardDeviationRelativeToOptimal * 0.3;
    }

    private double calculateAdjustedBudget(double safeBudgetConfidence, double pecentile90) {
        double budgetMultiplierPercent = 0.3d;
        double realisticBudget = budgetMultiplierPercent - (budgetMultiplierPercent * safeBudgetConfidence);

        return pecentile90 + pecentile90 * realisticBudget;
    }

    // TODO: Separate insights for each budget by category id
    private Double calculateBudgetUtilization(Integer categoryId, List<ExpenseResponse> expenseResponses) {
        double categoryBudget = budgetService.getAllByCategoryId(categoryId).stream()
                .mapToDouble(BudgetRequest::quota)
                .sum();
        if (categoryBudget == 0) {
            return null;
        }

        double categoryExpenses = expenseResponses
                .stream().mapToDouble(ExpenseResponse::cost)
                .sum();

        if (categoryExpenses > categoryBudget) {
            return 1.0d;
        }

        return categoryExpenses / categoryBudget;
    }

    private CategoryInsightsResponse calculateInsightDetails(CategoryResponse category, ExpenseFilterRequest expenseRequest, YearMonth yearMonth) {
        // TODO: Skip processing if there are empty categories
        CategoryInsightResultRow insightResponse = expenseTrackingService.insightsByCategory(expenseRequest, category.id());
        List<ExpenseResponse> expenses = expenseTrackingService.getExpenseByDate(expenseRequest);

        if (insightResponse == null) {
            return null;
        }

        double safeBudgetConfidence = calculateSafeBudgetConfidence(insightResponse);
        double adjustedSafeBudget = calculateAdjustedBudget(safeBudgetConfidence, insightResponse.getPercentile90());
        double budgetUtilization = calculateBudgetUtilization(category.id(), expenses);

        return new CategoryInsightsResponse(
                category.id(),
                yearMonth,
                category,
                insightResponse.getStdDevPercent(),
                insightResponse.getMedian(),
                adjustedSafeBudget,
                insightResponse.getExpenseCount(),
                LocalDate.now(),
                safeBudgetConfidence,
                budgetUtilization
        );
    }

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
                    .map(category -> calculateInsightDetails(category, avgExpenseRequest, yearMonth))
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
