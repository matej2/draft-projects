package com.matej2.expense_tracker.service.domain;

import com.matej2.expense_tracker.domain.dto.FrequencyResponse;
import com.matej2.expense_tracker.domain.entity.Frequency;
import com.matej2.expense_tracker.domain.mapper.FrequencyMapper;
import com.matej2.expense_tracker.exception.ResourceNotFoundException;
import com.matej2.expense_tracker.repository.domain.FrequencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FrequencyService {
    private final FrequencyRepository frequencyRepository;

    public synchronized List<FrequencyResponse> getFrequency() {
        return this.frequencyRepository.findAll().stream().map(FrequencyMapper::toResponse).toList();
    }

    public Frequency getFrequencyOrThrow(Integer id) {
        return this.frequencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }
}
