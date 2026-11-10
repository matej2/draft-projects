package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.model.entity.Category;
import com.matej2.budget_lens.model.entity.Insight;
import com.matej2.budget_lens.model.response.CategoryInsightsResponse;
import com.matej2.budget_lens.model.response.FrequencyResponse;
import com.matej2.budget_lens.model.response.InsightBudgetResponse;
import com.matej2.budget_lens.model.response.InsightsResponse;
import com.matej2.budget_lens.utils.MathUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InsightMapper implements Mapper<CategoryInsightsResponse, CategoryInsightsResponse, Insight>{
    private final CategoryMapper categoryMapper;

    public  Insight toEntity(CategoryInsightsResponse insight, Category category, YearMonth yearMonth) {
        Insight insightEntity = new Insight();
        insightEntity.setYearMonth(yearMonth);
        insightEntity.setCategory(category);
        insightEntity.setUpdatedAt(insight.updatedAt());
        insightEntity.setId(insight.id());
        insightEntity.setStandardDeviationPercent(insight.stddevPercent());
        insightEntity.setMedian(insight.median());
        insightEntity.setPercentile90(insight.percentile90());
        insightEntity.setExpenseCount(insight.expenseCount());
        insightEntity.setSafeBudgetConfidence(insight.safeBudgetConfidence());
        insightEntity.setBudgetUtilization(insight.budgetUtilization());

        return insightEntity;
    }

    @Override
    public Insight toEntity(CategoryInsightsResponse requestDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public  CategoryInsightsResponse toResponse(Insight insightEntity) {
        return new CategoryInsightsResponse(
                insightEntity.getId(),
                insightEntity.getYearMonth(),
                categoryMapper.toResponse(insightEntity.getCategory()),
                insightEntity.getStandardDeviationPercent(),
                insightEntity.getMedian(),
                insightEntity.getPercentile90(),
                insightEntity.getExpenseCount(),
                insightEntity.getUpdatedAt(),
                insightEntity.getSafeBudgetConfidence(),
                insightEntity.getBudgetUtilization()
        );
    }

    public InsightsResponse toResponse(CategoryInsightsResponse categoryInsightsResponse) {
        return new InsightsResponse(
                categoryInsightsResponse.id(),
                categoryInsightsResponse.yearMonth(),
                categoryInsightsResponse.category(),
                MathUtils.toPercentage(categoryInsightsResponse.stddevPercent()),
                categoryInsightsResponse.median(),
                categoryInsightsResponse.percentile90(),
                categoryInsightsResponse.expenseCount(),
                categoryInsightsResponse.updatedAt(),
                MathUtils.toPercentage(categoryInsightsResponse.safeBudgetConfidence()),
                MathUtils.toPercentage(categoryInsightsResponse.budgetUtilization())
        );
    }

    public static InsightBudgetResponse toResponse(CategoryInsightsResponse categoryInsights, Map<FrequencyResponse, BigDecimal> frequencyToBudget) {
        return new InsightBudgetResponse(
                frequencyToBudget,
                MathUtils.toPercentage(categoryInsights.safeBudgetConfidence()),
                MathUtils.toPercentage(categoryInsights.budgetUtilization()),
                categoryInsights.updatedAt());
    }
}
