package com.example.expense_tracker.domain.dto;

import java.util.List;

public record BudgetStatusResponse(
        List<CategoryBudget> categoryBudgetList,
        String name,
        Float targetAmount,
        Float currentAmount
) {}
