package com.matej2.budget_lens.domain.dto;

import com.matej2.budget_lens.utils.MathUtils;

import java.time.YearMonth;

public record CategoryInsightsResponse(Integer id, YearMonth yearMonth, CategoryResponse category, Float stdDev, Float stddevPercent, Float avg) {

    public CategoryInsightsResponse(
            Integer id,
            YearMonth yearMonth,
            CategoryResponse category,
            Float stdDev,
            Float stddevPercent,
            Float avg
    ) {
        this.id = id;
        this.yearMonth = yearMonth;
        this.category = category;
        this.stdDev = MathUtils.toTwoDecimals(stdDev);
        this.stddevPercent = MathUtils.toTwoDecimals(stddevPercent);
        this.avg = MathUtils.toTwoDecimals(avg);
    }

}
