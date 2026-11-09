package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.mapper.FrequencyMapper;
import com.matej2.budget_lens.model.entity.Frequency;
import com.matej2.budget_lens.model.response.CategoryInsightsResponse;
import com.matej2.budget_lens.model.response.FrequencyResponse;
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
        Frequency monthlyFrequency = frequencyRepository.findOneByNumber(12);
        List<Frequency> allFrequencies = frequencyRepository.findAll();
        Map<FrequencyResponse, Integer> frequencyToBudget = new HashMap<>();

        double budgetYearlyValue = insight.percentile90() * monthlyFrequency.getNumber();

        for (Frequency frequency : allFrequencies) {
            long relativeBudget = Math.round(budgetYearlyValue / frequency.getNumber());
            frequencyToBudget.put(
                    this.frequencyMapper.toResponse(frequency),
                    Math.toIntExact(relativeBudget)
            );
        }

        return frequencyToBudget;
    }

}
