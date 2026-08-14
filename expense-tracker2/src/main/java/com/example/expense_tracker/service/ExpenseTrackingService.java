package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.entity.Expense;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExpenseTrackingService {
    public synchronized Expense getExpense() {
        return new Expense(
                "nakup",
                15,
                new Date()
        );
    }
}
