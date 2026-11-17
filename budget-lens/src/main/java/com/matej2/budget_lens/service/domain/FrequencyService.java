package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.mapper.FrequencyMapper;
import com.matej2.budget_lens.model.entity.Frequency;
import com.matej2.budget_lens.model.response.CategoryInsightsResponse;
import com.matej2.budget_lens.model.response.FrequencyResponse;
import com.matej2.budget_lens.repository.domain.FrequencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FrequencyService {
    private final FrequencyRepository frequencyRepository;
    private final FrequencyMapper frequencyMapper = FrequencyMapper.INSTANCE;

    public synchronized List<FrequencyResponse> getAllFrequencies() {
        return this.frequencyRepository.findAll().stream().map(frequencyMapper::frequencyToFrequencyResponse).toList();
    }

    public Map<FrequencyResponse, BigDecimal> generateFrequencyToBudgetMapping(CategoryInsightsResponse insight) {
        Frequency monthlyFrequency = frequencyRepository.findOneByNumber(12);
        List<Frequency> allFrequencies = frequencyRepository.findAll();
        Map<FrequencyResponse, BigDecimal> frequencyToBudget = new HashMap<>();

        BigDecimal frequencyValue = BigDecimal.valueOf(monthlyFrequency.getNumber());
        BigDecimal budgetYearlyValue = insight.getPercentile90().multiply(frequencyValue);

        for (Frequency frequency : allFrequencies) {
            BigDecimal frequencyNumber = BigDecimal.valueOf(frequency.getNumber());
            BigDecimal relativeBudget = budgetYearlyValue.divide(frequencyNumber, RoundingMode.HALF_UP);
            frequencyToBudget.put(
                    this.frequencyMapper.frequencyToFrequencyResponse(frequency),
                    relativeBudget.setScale(0, RoundingMode.HALF_UP)
            );
        }

        return frequencyToBudget;
    }

}
