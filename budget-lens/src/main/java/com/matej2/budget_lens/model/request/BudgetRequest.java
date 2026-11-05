package com.matej2.budget_lens.model.request;

public record BudgetRequest(
        String note,
        Float quota,
        Integer categoryId,
        Integer frequencyId
) {
}
