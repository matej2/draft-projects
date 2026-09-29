package com.matej2.budget_lens.domain.dto;

public record CategoryInsightsResponse(Integer id, CategoryResponse category, Float stdDev, Float stddevPercent, Float avg) {

    public CategoryInsightsResponse(
            Integer id,
            CategoryResponse category,
            Float stdDev,
            Float stddevPercent,
            Float avg
    ) {
        this.id = id;
        this.category = category;
        this.stdDev = (float) (Math.round(stdDev * 100.0) / 100.0);
        this.stddevPercent = (float) (Math.round(stddevPercent * 100.0) / 100.0);
        this.avg = (float) (Math.round(avg * 100.0) / 100.0);
    }

}
