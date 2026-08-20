package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.entity.Expense;
import com.example.expense_tracker.domain.entity.Frequency;
import com.example.expense_tracker.exception.ResourceNotFoundException;
import com.example.expense_tracker.repository.ExpenseRepository;
import com.example.expense_tracker.repository.FrequencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseTrackingService {
    private final ExpenseRepository expenseRepository;
    private final FrequencyRepository frequencyRepository;

    public ExpenseTrackingService(
            ExpenseRepository expenseRepository,
            FrequencyRepository frequencyRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.frequencyRepository = frequencyRepository;
    }

    public synchronized void addExpense(Expense expense){
        this.expenseRepository.save(expense);
    }

    public synchronized List<Expense> getExpense() {
        return this.expenseRepository.findAll();
    }

    // TODO: Extract into new service or update existing
    public synchronized List<Frequency> getFrequency() {
        return this.frequencyRepository.findAll();
    }

    public synchronized Frequency getFrequency(Integer id) {
        return this.frequencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }
}
