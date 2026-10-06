package com.matej2.budget_lens.domain.dto.response;

import java.time.LocalDate;
import java.time.YearMonth;

public record CategoryInsightsResponse(
        Integer id,
        YearMonth yearMonth,
        CategoryResponse category,
        Float stddevPercent,
        Float median,
        Float percentile90,
        Long expenseCount,
        LocalDate updatedAt,
        Float safeBudgetConfidence
        ) {}
