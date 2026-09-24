package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.entity.Insight;

public class InsightMapper {
    public static Insight toInsightEntity(CategoryInsightsResponse insight) {
        Insight insightEntity = new Insight();
        insightEntity.setAverage(insight.avg());
        insightEntity.setStandardDeviation(insight.stdDev());
        insightEntity.setStandardDeviationPercent(insight.stddevPercent());

        return insightEntity;
    }
}
