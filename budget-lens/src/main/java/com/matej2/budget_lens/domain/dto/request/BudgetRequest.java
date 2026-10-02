package com.matej2.budget_lens.domain.dto.request;

public record BudgetRequest(
        String note,
        Float quota,
        Integer categoryId,
        Integer frequencyId
) {
}
