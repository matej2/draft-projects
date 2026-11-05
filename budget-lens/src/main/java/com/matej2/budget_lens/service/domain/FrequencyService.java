package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.model.response.CategoryInsightsResponse;
import com.matej2.budget_lens.model.response.FrequencyResponse;
import com.matej2.budget_lens.mapper.FrequencyMapper;
import com.matej2.budget_lens.repository.domain.FrequencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FrequencyService {
    private final FrequencyRepository frequencyRepository;
    private final FrequencyMapper frequencyMapper;

    public synchronized List<FrequencyResponse> getAllFrequencies() {
        return this.frequencyRepository.findAll().stream().map(frequencyMapper::toResponse).toList();
    }

    public Map<FrequencyResponse, Integer> generateFrequencyToBudgetMapping(CategoryInsightsResponse insight) {
        List<FrequencyResponse> allFrequencies = this.getAllFrequencies();
        Map<FrequencyResponse, Integer> frequencyToBudget = new HashMap<>();

        FrequencyResponse monthlyFrequency = allFrequencies.stream()
                .filter(f -> f.number() == 12)
                .findFirst()
                .orElseThrow();

        double budgetYearlyValue = insight.percentile90() * monthlyFrequency.number();

        for (FrequencyResponse frequency : allFrequencies) {
            long relativeBudget = Math.round(budgetYearlyValue / frequency.number());
            frequencyToBudget.put(frequency, Math.toIntExact(relativeBudget));
        }

        return frequencyToBudget;
    }

}
