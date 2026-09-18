package com.example.expense_tracker.domain.mapper;

import com.example.expense_tracker.domain.dto.CategoryInsightsResponse;
import com.example.expense_tracker.domain.entity.Insight;

public class InsightMapper {
    public static Insight toInsightEntity(CategoryInsightsResponse insight) {
        Insight insightEntity = new Insight();
        insightEntity.setAverage(insight.avg());
        insightEntity.setStandardDeviation(insight.stdDev());
        insightEntity.setStandardDeviationPercent(insight.stddevPercent());

        return insightEntity;
    }
}
