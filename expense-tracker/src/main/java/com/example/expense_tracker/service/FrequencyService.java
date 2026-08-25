package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.dto.FrequencyResponse;
import com.example.expense_tracker.domain.entity.Frequency;
import com.example.expense_tracker.domain.mapper.FrequencyMapper;
import com.example.expense_tracker.exception.ResourceNotFoundException;
import com.example.expense_tracker.repository.FrequencyRepository;
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
