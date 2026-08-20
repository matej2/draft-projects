package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.entity.Expense;
import com.example.expense_tracker.domain.entity.Frequency;
import com.example.expense_tracker.domain.mapper.ExpenseMapper;
import com.example.expense_tracker.exception.ResourceNotFoundException;
import com.example.expense_tracker.repository.ExpenseRepository;
import com.example.expense_tracker.repository.FrequencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseTrackingService {
    private final ExpenseRepository expenseRepository;
    private final FrequencyRepository frequencyRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseTrackingService(
            ExpenseRepository expenseRepository,
            FrequencyRepository frequencyRepository, ExpenseMapper expenseMapper
    ) {
        this.expenseRepository = expenseRepository;
        this.frequencyRepository = frequencyRepository;
        this.expenseMapper = expenseMapper;
    }

    public synchronized void addExpense(ExpenseRequest expense){
        Frequency frequency = this.getFrequency(expense.frequency_id());
        Expense mappedExpense = this.expenseMapper.fromExpenseRequest(expense);
        mappedExpense.setFrequency_id(frequency);

        this.expenseRepository.save(mappedExpense);
    }

    public synchronized List<ExpenseResponse> getExpense() {
        return this.expenseRepository.findAll().stream()
                .map(ExpenseMapper::toExpenseResponse)
                .toList();
    }

    // TODO: Extract into new service or update existing
    public synchronized List<Frequency> getFrequency() {
        return this.frequencyRepository.findAll();
    }

    public synchronized Frequency getFrequency(Integer id) {
        return this.frequencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }
}
