package com.matej2.budget_lens.domain.dto.response;

public record BudgetStatusResponse(
        CurrentCategoryBudgetResponse currentCategoryBudgetResponse,
        String name,
        Float targetAmount,
        Float currentAmount
) {}
