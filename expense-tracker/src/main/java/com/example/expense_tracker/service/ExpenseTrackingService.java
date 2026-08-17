package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.entity.Expense;
import com.example.expense_tracker.domain.entity.Frequency;
import com.example.expense_tracker.repository.ExpenseRepository;
import com.example.expense_tracker.repository.FrequencyRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

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
    public synchronized List<Expense> getExpense() {
        Expense newExpense = new Expense(
                "nakup",
                15,
                new Date()
        );
        this.expenseRepository.save(newExpense);

        return this.expenseRepository.findAll();
    }

    // TODO: Extract into new service or update existing
    public synchronized List<Frequency> getFrequency() {
        return this.frequencyRepository.findAll();
    }
}
