package com.matej2.budget_lens.domain.dto.response;

import java.time.YearMonth;
import java.util.Date;

public record CategoryInsightsResponse(
        Integer id,
        YearMonth yearMonth,
        CategoryResponse category,
        Float stdDev,
        Float stddevPercent,
        Float median,
        Float percentile90,
        Boolean isCostVariable,
        Long expenseCount,
        Date updatedAt
        ) {}
