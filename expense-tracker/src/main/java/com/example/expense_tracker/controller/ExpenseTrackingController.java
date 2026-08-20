package com.example.expense_tracker.controller;

import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.entity.Frequency;
import com.example.expense_tracker.domain.mapper.ExpenseMapper;
import com.example.expense_tracker.service.ExpenseTrackingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExpenseTrackingController {
    private final ExpenseTrackingService expenseTrackingService;
    private final ExpenseMapper expenseMapper;

    public ExpenseTrackingController(
            ExpenseTrackingService expenseTrackingService,
            ExpenseMapper expenseMapper) {
        this.expenseTrackingService = expenseTrackingService;
        this.expenseMapper = expenseMapper;
    }

    @GetMapping("/")
    public String home(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/expenses")
    public List<ExpenseResponse> getExpense(){
        return this.expenseTrackingService.getExpense();
    }

    @PostMapping("/expense")
    public void saveExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        this.expenseTrackingService.addExpense(expenseRequest);
    }

    @PutMapping("/expense/{id}")
    public void saveExpense(@PathVariable Integer id, @Valid @RequestBody ExpenseRequest expenseRequest) {
        this.expenseTrackingService.updateExpense(id, expenseRequest);
    }


    @GetMapping("/frequency")
    public List<Frequency> getExpenseFrequency(){
        return this.expenseTrackingService.getFrequency();
    }
}
