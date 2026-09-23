package com.matej2.expense_tracker.domain.dto;

public record BudgetStatusResponse(
        CurrentCategoryBudgetResponse currentCategoryBudgetResponse,
        String name,
        Float targetAmount,
        Float currentAmount
) {}
