package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.domain.dto.request.ExpenseFilterRequest;
import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.dto.response.CategoryResponse;
import com.matej2.budget_lens.domain.dto.response.ExpenseResponse;
import com.matej2.budget_lens.domain.jpa.CategoryInsightResultRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsightCalculatorService {
    private final BudgetService budgetService;
    private final ExpenseTrackingService expenseTrackingService;

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
    public Double calculateBudgetUtilization(Integer categoryId, List<ExpenseResponse> expenseResponses) {
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

    public CategoryInsightsResponse calculateInsightDetails(CategoryResponse category, ExpenseFilterRequest expenseRequest, YearMonth yearMonth) {
        // TODO: Skip processing if there are empty categories
        CategoryInsightResultRow insightResponse = expenseTrackingService.insightsByCategory(expenseRequest, category.id());
        List<ExpenseResponse> expenses = expenseTrackingService.getExpenseByDate(expenseRequest);

        if (insightResponse == null) {
            return null;
        }

        double safeBudgetConfidence = calculateSafeBudgetConfidence(insightResponse);
        double adjustedSafeBudget = calculateAdjustedBudget(safeBudgetConfidence, insightResponse.getPercentile90());
        Double budgetUtilization = calculateBudgetUtilization(category.id(), expenses);

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
}
