package com.example.expense_tracker.domain.mapper;

import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.entity.Expense;
import com.example.expense_tracker.domain.entity.Frequency;
import com.example.expense_tracker.service.ExpenseTrackingService;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    private ExpenseTrackingService expenseTrackingService;

    public ExpenseMapper(ExpenseTrackingService expenseTrackingService) {
        this.expenseTrackingService = expenseTrackingService;
    }

    public Expense fromExpenseRequest(ExpenseRequest expenseRequest) {
        Frequency frequency = expenseTrackingService.getFrequency(expenseRequest.frequency_id());

        Expense expense = new Expense();
        expense.setNote(expenseRequest.note());
        expense.setCost(expenseRequest.cost());
        expense.setExpense_date(expenseRequest.expense_date());
        expense.setFrequency_id(frequency);

        return expense;
    }

    public ExpenseResponse toExpenseResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getNote(),
                expense.getCost(),
                expense.getExpense_date(),
                expense.getFrequency_id().getId()
        );
    }
}
