package com.example.expense_tracker.controller;

import com.example.expense_tracker.domain.entity.Expense;
import com.example.expense_tracker.service.ExpenseTrackingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExpenseTrackingController {
    private final ExpenseTrackingService expenseTrackingService;

    public ExpenseTrackingController(ExpenseTrackingService expenseTrackingService) {
        this.expenseTrackingService = expenseTrackingService;
    }

    @GetMapping("/get")
    public Expense getExpense(){
        return this.expenseTrackingService.getExpense();
    }
}
