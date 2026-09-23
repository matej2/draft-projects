package com.matej2.expense_tracker.domain.dto;

public record CurrentCategoryBudgetResponse(
        String category,
        Float monthlyLimit,
        Float currentAmount
) {
}
