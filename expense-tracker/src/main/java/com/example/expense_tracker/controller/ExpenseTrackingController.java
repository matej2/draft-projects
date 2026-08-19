package com.example.expense_tracker.controller;

import com.example.expense_tracker.domain.entity.Expense;
import com.example.expense_tracker.domain.entity.Frequency;
import com.example.expense_tracker.service.ExpenseTrackingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExpenseTrackingController {
    private final ExpenseTrackingService expenseTrackingService;

    public ExpenseTrackingController(
            ExpenseTrackingService expenseTrackingService) {
        this.expenseTrackingService = expenseTrackingService;

    }

    @GetMapping("/")
    public Principal home(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/get")
    public List<Expense> getExpense(){
        return this.expenseTrackingService.getExpense();
    }

    @GetMapping("/frequency")
    public List<Frequency> getExpenseFrequency(){
        return this.expenseTrackingService.getFrequency();
    }
}
