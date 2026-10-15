package com.matej2.budget_lens.domain.dto.response;

public record BudgetResponse(
        String note,
        Float quota,
        Integer categoryId,
        Integer frequencyId
) {
}
