package com.example.expense_tracker.domain.dto;

public record CategoryBudget(
        String category,
        Float monthlyLimit,
        Byte month
) {
}
