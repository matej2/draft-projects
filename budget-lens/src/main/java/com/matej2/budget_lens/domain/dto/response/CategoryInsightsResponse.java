package com.matej2.budget_lens.domain.dto.response;

import java.time.LocalDate;
import java.time.YearMonth;

public record CategoryInsightsResponse(
        Integer id,
        YearMonth yearMonth,
        CategoryResponse category,
        Double stddevPercent,
        Double median,
        Double percentile90,
        Long expenseCount,
        LocalDate updatedAt,
        Double safeBudgetConfidence,
        Double budgetUtilization
        ) {}
