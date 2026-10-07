package com.matej2.budget_lens.utils;

import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;

public class UserInterfaceUtils {
    public static String getClassNamesForInsightRow(CategoryInsightsResponse insight) {
        String result = "";
        boolean isInsightValid = checkInsightValidity(insight);

        if (insight.safeBudgetConfidence() > 50 && isInsightValid) {
            result += "candidate";
        }
        if (!checkInsightValidity(insight)) {
            result += "invalid";
        }
        return result;
    }
    public static boolean checkInsightValidity(CategoryInsightsResponse insight) {
        return insight.expenseCount() > 2;
    }
}
