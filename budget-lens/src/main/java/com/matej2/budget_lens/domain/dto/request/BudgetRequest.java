package com.matej2.budget_lens.domain.dto.request;

// TODO: Sort classes by request and response
public record BudgetRequest(
        String note,
        Float quota,
        Integer categoryId,
        Integer frequencyId
) {
}
