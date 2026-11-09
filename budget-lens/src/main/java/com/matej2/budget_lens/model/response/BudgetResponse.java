package com.matej2.budget_lens.model.response;

import java.math.BigDecimal;

public record BudgetResponse(
        String note,
        BigDecimal quota,
        Integer categoryId,
        Integer frequencyId
) {
}
