package com.matej2.budget_lens.utils;

import com.matej2.budget_lens.domain.dto.response.InsightsResponse;

public class UserInterfaceUtils {
    public static String getClassNamesForInsightRow(InsightsResponse insight) {
        String result = "";
        boolean isInsightValid = checkInsightValidity(insight);

        if (insight.safeBudgetConfidence() > 50 && isInsightValid && (insight.budgetUtilization() > 1 || insight.budgetUtilization() < 0.3)) {
            result += "candidate";
        }
        if (!checkInsightValidity(insight)) {
            result += "invalid";
        }
        return result;
    }
    public static boolean checkInsightValidity(InsightsResponse insight) {
        return insight.expenseCount() > 2;
    }
}
