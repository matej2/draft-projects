package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Insight;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class InsightMapper implements Mapper<CategoryInsightsResponse, CategoryInsightsResponse, Insight>{
    private final CategoryMapper categoryMapper;

    public  Insight toEntity(CategoryInsightsResponse insight, Category category, YearMonth yearMonth) {
        Insight insightEntity = new Insight();
        insightEntity.setId(insight.id());
        insightEntity.setYearMonth(yearMonth);
        insightEntity.setCategory(category);
        insightEntity.setStandardDeviationPercent(insight.stddevPercent());
        insightEntity.setMedian(insight.median());
        insightEntity.setPercentile90(insight.percentile90());
        insightEntity.setExpenseCount(insight.expenseCount());
        insightEntity.setUpdatedAt(insight.updatedAt());
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
}
