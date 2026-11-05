package com.matej2.budget_lens.model.response;

public record BudgetResponse(
        String note,
        Float quota,
        Integer categoryId,
        Integer frequencyId
) {
}
