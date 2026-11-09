package com.matej2.budget_lens.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record InsightsResponse(
        Integer id,
        YearMonth yearMonth,
        CategoryResponse category,
        Integer stddevPercent,
        BigDecimal median,
        BigDecimal percentile90,
        Long expenseCount,
        LocalDate updatedAt,
        Integer safeBudgetConfidence,
        Integer budgetUtilization
) {}
