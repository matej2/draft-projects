package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.dto.CategoryInsightsResponse;
import com.example.expense_tracker.domain.entity.Insight;
import com.example.expense_tracker.domain.mapper.InsightMapper;
import com.example.expense_tracker.repository.InsightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsightService {
    private final InsightRepository insightRepository;

    public void saveInsightList(List<CategoryInsightsResponse> insight) {
        List<Insight> insightListToSave =  insight.stream()
                .map(InsightMapper::toInsightEntity)
                .toList();
        insightRepository.saveAll(insightListToSave);
    }
}
