package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Insight;

import java.time.YearMonth;

public class InsightMapper {
    public static Insight toInsightEntity(CategoryInsightsResponse insight, Category category, YearMonth yearMonth) {

        Insight insightEntity = new Insight();
        insightEntity.setId(insight.id());
        insightEntity.setYearMonth(yearMonth);
        insightEntity.setCategory(category);
        insightEntity.setAverage(insight.avg());
        insightEntity.setStandardDeviation(insight.stdDev());
        insightEntity.setStandardDeviationPercent(insight.stddevPercent());

        return insightEntity;
    }

    public static CategoryInsightsResponse toResponse(Insight insightEntity) {
        return new CategoryInsightsResponse(
                insightEntity.getId(),
                insightEntity.getYearMonth(),
                CategoryMapper.toResponse(insightEntity.getCategory()),
                insightEntity.getStandardDeviation(),
                insightEntity.getStandardDeviationPercent(),
                insightEntity.getAverage()
        );
    }
}
