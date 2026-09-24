package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.FrequencyResponse;
import com.matej2.budget_lens.domain.entity.Frequency;
import com.matej2.budget_lens.domain.mapper.FrequencyMapper;
import com.matej2.budget_lens.exception.ResourceNotFoundException;
import com.matej2.budget_lens.repository.domain.FrequencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FrequencyService {
    private final FrequencyRepository frequencyRepository;

    // TODO: Rename
    public synchronized List<FrequencyResponse> getAllFrequencies() {
        return this.frequencyRepository.findAll().stream().map(FrequencyMapper::toResponse).toList();
    }

    public Frequency getFrequencyOrThrow(Integer id) {
        return this.frequencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }
}
