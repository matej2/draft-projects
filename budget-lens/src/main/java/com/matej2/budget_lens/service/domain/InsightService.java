package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.entity.Insight;
import com.matej2.budget_lens.domain.mapper.InsightMapper;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
import com.matej2.budget_lens.repository.domain.InsightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class InsightService {
    private final InsightRepository insightRepository;
    private final CategoryRepository categoryRepository;

    public void saveInsightList(List<CategoryInsightsResponse> insight, YearMonth yearMonth) {
        log.debug("Saved insights request for month");

        insight
            .forEach(i -> {
                Insight insightEntity = InsightMapper.toInsightEntity(
                        i,
                        categoryRepository.getReferenceById(i.id()),
                        yearMonth);

                insightRepository.updateByCategoryIdAndDate(
                        insightEntity.getAverage(),
                        insightEntity.getStandardDeviation(),
                        insightEntity.getStandardDeviationPercent(),
                        insightEntity.getCategory(),
                        yearMonth
                );
            });
    }

    public List<CategoryInsightsResponse> getAllInsights() {
        return insightRepository.findAll().stream()
                .map(InsightMapper::toResponse)
                .toList();
    };
}
