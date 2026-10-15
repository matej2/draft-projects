package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.response.FrequencyResponse;
import com.matej2.budget_lens.domain.mapper.FrequencyMapper;
import com.matej2.budget_lens.repository.domain.FrequencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FrequencyService {
    private final FrequencyRepository frequencyRepository;
    private final FrequencyMapper frequencyMapper;

    public synchronized List<FrequencyResponse> getAllFrequencies() {
        return this.frequencyRepository.findAll().stream().map(frequencyMapper::toResponse).toList();
    }

}
