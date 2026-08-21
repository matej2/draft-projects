package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.dto.FrequencyResponse;
import com.example.expense_tracker.domain.entity.Expense;
import com.example.expense_tracker.domain.entity.Frequency;
import com.example.expense_tracker.domain.mapper.ExpenseMapper;
import com.example.expense_tracker.domain.mapper.FrequencyMapper;
import com.example.expense_tracker.exception.ResourceNotFoundException;
import com.example.expense_tracker.repository.ExpenseRepository;
import com.example.expense_tracker.repository.FrequencyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        Frequency frequency = this.getFrequencyOrThrow(expense.frequencyId());
        Expense mappedExpense = this.expenseMapper.fromExpenseRequest(expense);
        mappedExpense.setFrequency(frequency);

        this.expenseRepository.save(mappedExpense);
    }

    public synchronized List<ExpenseResponse> getExpense() {
        return this.expenseRepository.findAll().stream()
                .map(ExpenseMapper::toExpenseResponse)
                .toList();
    }

    // In real world scenario I would use mapper that would update only defined properties
    // For simplicity purposes I override whole record
    public synchronized void updateExpense(Integer id, ExpenseRequest expenseRequest) {
        Expense mappedExpense = this.expenseMapper.fromExpenseRequest(expenseRequest);

        mappedExpense.setId(id);
        mappedExpense.setFrequency(this.getFrequencyOrThrow(expenseRequest.frequencyId()));

        this.expenseRepository.save(mappedExpense);
    }


    public List<ExpenseResponse> getExpenseByDate(LocalDate startDate, LocalDate endDate) {
        List<Expense> filteredExpense = this.expenseRepository.findByExpenseDateBetween(startDate, endDate);

        return filteredExpense.stream().map(ExpenseMapper::toExpenseResponse).toList();
    }

    // TODO: Extract into new service or update existing
    public synchronized List<FrequencyResponse> getFrequency() {
        return this.frequencyRepository.findAll().stream().map(FrequencyMapper::toResponse).toList();
    }

    private Frequency getFrequencyOrThrow(Integer id) {
        return this.frequencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }
}
