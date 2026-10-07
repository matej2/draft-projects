package com.matej2.budget_lens.domain.dto.response;

import java.time.LocalDate;
import java.time.YearMonth;

public record InsightsResponse(
        Integer id,
        YearMonth yearMonth,
        CategoryResponse category,
        Integer stddevPercent,
        Integer median,
        Integer percentile90,
        Long expenseCount,
        LocalDate updatedAt,
        Integer safeBudgetConfidence,
        Integer budgetUtilization
) {}
