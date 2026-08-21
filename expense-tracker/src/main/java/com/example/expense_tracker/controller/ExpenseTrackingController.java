package com.example.expense_tracker.controller;

import com.example.expense_tracker.domain.dto.ExpenseFilterRequest;
import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.dto.FrequencyResponse;
import com.example.expense_tracker.service.ExpenseTrackingService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public String home(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense found"),
    })
    public List<ExpenseResponse> getExpense(){
        return this.expenseTrackingService.getExpense();
    }

    @PostMapping("/expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid expense request body")
    })
    public void saveExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        this.expenseTrackingService.addExpense(expenseRequest);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense updated successfully"),
            @ApiResponse(responseCode = "400", description = "Expense id does not exist")
    })
    @PutMapping("/expense/{id}")
    public void updateExpense(@PathVariable Integer id, @Valid @RequestBody ExpenseRequest expenseRequest) {
        this.expenseTrackingService.updateExpense(id, expenseRequest);
    }

    @GetMapping("/expense/filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense updated successfully"),
            @ApiResponse(responseCode = "400", description = "Expense id does not exist")
    })
    public List<ExpenseResponse> filterExpense(@Valid @RequestBody ExpenseFilterRequest expenseFilterRequest){
        return this.expenseTrackingService.getExpenseByDate(expenseFilterRequest.startDate(), expenseFilterRequest.endDate());
    }

    @GetMapping("/frequency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frequency found"),
    })
    public List<FrequencyResponse> getExpenseFrequency(){
        return this.expenseTrackingService.getFrequency();
    }
}
