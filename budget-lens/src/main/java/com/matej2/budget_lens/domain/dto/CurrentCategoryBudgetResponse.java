package com.matej2.budget_lens.domain.dto;

public record CurrentCategoryBudgetResponse(
        String category,
        Float monthlyLimit,
        Float currentAmount
) {
}
