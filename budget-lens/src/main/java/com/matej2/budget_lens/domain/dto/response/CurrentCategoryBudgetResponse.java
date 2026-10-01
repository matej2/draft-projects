package com.matej2.budget_lens.domain.dto.response;

public record CurrentCategoryBudgetResponse(
        String category,
        Float monthlyLimit,
        Float currentAmount
) {
}
