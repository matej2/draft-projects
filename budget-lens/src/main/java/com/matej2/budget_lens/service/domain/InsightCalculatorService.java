package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.model.entity.Budget;
import com.matej2.budget_lens.model.entity.Expense;
import com.matej2.budget_lens.model.jpa.CategoryInsightResultRow;
import com.matej2.budget_lens.model.request.ExpenseFilterRequest;
import com.matej2.budget_lens.model.response.CategoryInsightsResponse;
import com.matej2.budget_lens.model.response.CategoryResponse;
import com.matej2.budget_lens.repository.domain.BudgetRepository;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsightCalculatorService {
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    private double getConfidenceBasedOnCount(double expenseCount) {
        byte optimalExpenseCount = 30;
        double expenseCountConfidence;

        if (expenseCount < 2) {
            return 0d;
        }

        if (expenseCount < optimalExpenseCount) {
            expenseCountConfidence = expenseCount / 28.0;
        } else {
            expenseCountConfidence = 1d;
        }
        return expenseCountConfidence;
    }

    private double getConfidenceBasedOnStandardDeviation(double stdDev) {
        if (stdDev >= 1d) {
            return 0d;
        }

        return 1.0 - stdDev;
    }

    private double calculateSafeBudgetConfidence(CategoryInsightResultRow insightResponse) {
        double expenseCountConfidence = this.getConfidenceBasedOnCount(insightResponse.getExpenseCount());
        if (expenseCountConfidence == 0) {
            return 0d;
        }

        double expenseStandardDeviationConfidence = this.getConfidenceBasedOnStandardDeviation(insightResponse.getStdDevPercent());

        return expenseCountConfidence * 0.7 + expenseStandardDeviationConfidence * 0.3;
    }

    private double calculateSafeBudget(double safeBudgetConfidence, double pecentile90) {
        double budgetMultiplierPercent = 0.3d;
        double realisticBudget = budgetMultiplierPercent - (budgetMultiplierPercent * safeBudgetConfidence);

        return pecentile90 + pecentile90 * realisticBudget;
    }

    public Double calculateBudgetUtilization(Integer categoryId, List<Expense> expenseResponses) {
        double totalCategoryBudget = budgetRepository.findAllByCategoryId(categoryId).stream()
                .mapToDouble(Budget::getQuota)
                .sum();
        if (totalCategoryBudget == 0) {
            return null;
        }

        double totalCategoryExpenses = expenseResponses
                .stream().mapToDouble(Expense::getCost)
                .sum();

        if (totalCategoryExpenses > totalCategoryBudget) {
            return 1.0d;
        }

        return totalCategoryExpenses / totalCategoryBudget;
    }

    public CategoryInsightsResponse calculateInsightDetails(CategoryResponse category, ExpenseFilterRequest expenseRequest, YearMonth yearMonth) {
        CategoryInsightResultRow insightResponse = expenseRepository.getInsightsByCategoryIdByDateBetween(
                category.id(),
                expenseRequest.startDate(),
                expenseRequest.endDate()
        );
        if (insightResponse == null) {
            return null;
        }

        List<Expense> expenseList = expenseRepository.findByExpenseDateBetween(
                expenseRequest.startDate(), expenseRequest.endDate()
        );

        double safeBudgetConfidence = calculateSafeBudgetConfidence(insightResponse);
        double adjustedSafeBudget = calculateSafeBudget(safeBudgetConfidence, insightResponse.getPercentile90());

        Double budgetUtilization = null;
        if (!expenseList.isEmpty()) {
            budgetUtilization = calculateBudgetUtilization(category.id(), expenseList);
        }

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
