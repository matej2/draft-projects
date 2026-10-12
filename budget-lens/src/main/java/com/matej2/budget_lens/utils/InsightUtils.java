package com.matej2.budget_lens.utils;

import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.dto.response.FrequencyResponse;
import com.matej2.budget_lens.domain.dto.response.InsightBudgetResponse;
import com.matej2.budget_lens.domain.dto.response.InsightsResponse;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InsightUtils {
    public static List<InsightsResponse> getFormatedInsights(List<CategoryInsightsResponse> insightList) {
        return insightList.stream()
                .map(i -> new InsightsResponse(
                        i.id(),
                        i.yearMonth(),
                        i.category(),
                        MathUtils.toPercentage(i.stddevPercent()),
                        MathUtils.toTwoDecimals(i.median()),
                        MathUtils.toTwoDecimals(i.percentile90()),
                        i.expenseCount(),
                        i.updatedAt(),
                        MathUtils.toPercentage(i.safeBudgetConfidence()),
                        MathUtils.toPercentage(i.budgetUtilization())
                ))
                .toList();
    }

    public static List<InsightBudgetResponse> getFormattedBudgetInsight(List<CategoryInsightsResponse> categoryInsights, Map<FrequencyResponse, Integer> frequencyToBudget) {
        return categoryInsights.stream()
                .map(insight -> new InsightBudgetResponse(
                        frequencyToBudget,
                        MathUtils.toPercentage(insight.safeBudgetConfidence()),
                        MathUtils.toPercentage(insight.budgetUtilization()),
                        insight.updatedAt()
                ))
                .toList();
    }

    public static Map<YearMonth, List<InsightsResponse>> getGroupedInsights(List<InsightsResponse> insightList) {
        return insightList.stream()
                .collect(Collectors.groupingBy(
                        InsightsResponse::yearMonth,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }
}
