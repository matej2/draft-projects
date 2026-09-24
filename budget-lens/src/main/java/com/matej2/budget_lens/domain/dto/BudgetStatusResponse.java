package com.matej2.budget_lens.domain.dto;

public record BudgetStatusResponse(
        CurrentCategoryBudgetResponse currentCategoryBudgetResponse,
        String name,
        Float targetAmount,
        Float currentAmount
) {}
