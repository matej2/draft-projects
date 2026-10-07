package com.matej2.budget_lens.utils;

import com.matej2.budget_lens.domain.entity.Budget;
import com.matej2.budget_lens.domain.entity.Frequency;

public class BudgetUtils {
    public static float getNormalizedBudget(Budget currentBudget, byte newFrequency) {
        Frequency frequency = currentBudget.getFrequency();
        float yearlyBudget = currentBudget.getQuota() * frequency.getNumber();

        return yearlyBudget / newFrequency;
    }
}
