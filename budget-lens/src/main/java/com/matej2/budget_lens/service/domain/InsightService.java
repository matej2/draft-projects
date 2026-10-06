package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.entity.Insight;
import com.matej2.budget_lens.domain.mapper.InsightMapper;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
import com.matej2.budget_lens.repository.domain.InsightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class InsightService {
    private final InsightRepository insightRepository;
    private final CategoryRepository categoryRepository;
    private final InsightMapper insightMapper;

    public void saveInsightList(List<CategoryInsightsResponse> insight, YearMonth yearMonth) {
        log.debug("Saved insights request for month");

        insight
            .forEach(i -> {
                Insight insightEntity = insightMapper.toEntity(
                        i,
                        categoryRepository.getReferenceById(i.id()),
                        yearMonth);

                insightRepository.updateByCategoryIdAndDate(
                        insightEntity.getStandardDeviationPercent(),
                        insightEntity.getCategory(),
                        yearMonth,
                        insightEntity.getMedian(),
                        insightEntity.getPercentile90(),
                        insightEntity.getExpenseCount(),
                        insightEntity.getUpdatedAt(),
                        insightEntity.getSafeBudgetConfidence()
                );

            });
    }

    public List<CategoryInsightsResponse> getAllInsights() {
        Sort.Order yearMonthOrder = new Sort.Order(Sort.Direction.DESC, "yearMonth");
        Sort.Order categoryOrder = new Sort.Order(Sort.Direction.ASC, "category");

        List<Sort.Order> orders = List.of(yearMonthOrder, categoryOrder);

        return insightRepository.findAll(Sort.by(orders)).stream()
                .map(insightMapper::toResponse)
                .toList();
    };
}
