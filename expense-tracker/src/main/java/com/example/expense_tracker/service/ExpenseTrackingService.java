package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.entity.Expense;
import com.example.expense_tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExpenseTrackingService {
    private final ExpenseRepository expenseRepository;

    public ExpenseTrackingService(
            ExpenseRepository expenseRepository
    ) {
        this.expenseRepository = expenseRepository;
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
}
