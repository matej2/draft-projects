package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.model.entity.Insight;
import com.matej2.budget_lens.model.response.CategoryInsightsResponse;
import com.matej2.budget_lens.model.response.InsightBudgetResponse;
import com.matej2.budget_lens.model.response.InsightsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CommonMapperUtils.class)
public interface InsightMapper {
    InsightMapper INSTANCE = Mappers.getMapper(InsightMapper.class);

    Insight categoryInsightResponseToInsight(CategoryInsightsResponse categoryInsightsResponse);

    CategoryInsightsResponse insightToCategoryInsightResponse(Insight insightEntity);

    @Mapping(source = "standardDeviationPercent", target = "standardDeviationPercent", qualifiedByName = "toPercent")
    @Mapping(source = "safeBudgetConfidence", target = "safeBudgetConfidence", qualifiedByName = "toPercent")
    @Mapping(source = "budgetUtilization", target = "budgetUtilization", qualifiedByName = "toPercent")
    @Mapping(target = "median", qualifiedByName = "toTwoDecimals")
    @Mapping(target = "percentile90", qualifiedByName = "toTwoDecimals")
    InsightsResponse categoryInsightResponseToInsightResponse(CategoryInsightsResponse categoryInsightsResponse);

    @Mapping(target = "budgetUtilization", qualifiedByName = "toPercent")
    @Mapping(target = "safeBudgetConfidence", qualifiedByName = "toPercent")
    @Mapping(source = "updatedAt", target = "insightUpdatedAt")
    InsightBudgetResponse categoryInsightResponseToInsightBudgetResponse(CategoryInsightsResponse categoryInsightsResponse);
}
