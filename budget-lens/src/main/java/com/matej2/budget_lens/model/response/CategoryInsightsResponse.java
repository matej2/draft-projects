package com.matej2.budget_lens.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record CategoryInsightsResponse(
        Integer id,
        YearMonth yearMonth,
        CategoryResponse category,
        Double stddevPercent,
        BigDecimal median,
        BigDecimal percentile90,
        Long expenseCount,
        LocalDate updatedAt,
        Double safeBudgetConfidence,
        Double budgetUtilization
        ) {}
