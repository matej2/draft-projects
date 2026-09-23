package com.example.expense_tracker.service.domain;

import com.example.expense_tracker.domain.dto.CategoryInsightsResponse;
import com.example.expense_tracker.domain.entity.Insight;
import com.example.expense_tracker.domain.mapper.InsightMapper;
import com.example.expense_tracker.repository.domain.InsightRepository;
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
