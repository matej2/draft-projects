package com.example.expense_tracker.domain.dto;

public record CurrentCategoryBudgetResponse(
        String category,
        Float monthlyLimit,
        Float currentAmount
) {
}
