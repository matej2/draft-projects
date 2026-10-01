package com.matej2.budget_lens.domain.dto;

public record BudgetRequest(
        String note,
        Float quota,
        Integer categoryId,
        Integer frequencyId
) {
}
