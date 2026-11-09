package com.matej2.budget_lens.model.request;

import java.math.BigDecimal;

public record BudgetRequest(
        String note,
        BigDecimal quota,
        Integer categoryId,
        Integer frequencyId
) {
}
