package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.entity.Insight;
import com.matej2.budget_lens.domain.mapper.InsightMapper;
import com.matej2.budget_lens.repository.domain.InsightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class InsightService {
    private final InsightRepository insightRepository;

    public void saveInsightList(List<CategoryInsightsResponse> insight) {
        log.debug("Saved insights request for month");

        List<Insight> insightListToSave =  insight.stream()
                .map(InsightMapper::toInsightEntity)
                .toList();
        insightRepository.saveAll(insightListToSave);
    }
}
