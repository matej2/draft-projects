package com.example.expense_tracker.domain.mapper;

import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public Expense fromExpenseRequest(ExpenseRequest expenseRequest) {
        Expense expense = new Expense();
        expense.setNote(expenseRequest.note());
        expense.setCost(expenseRequest.cost());
        expense.setExpenseDate(expenseRequest.expense_date());

        return expense;
    }

    public static ExpenseResponse toExpenseResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getNote(),
                expense.getCost(),
                expense.getExpenseDate(),
                expense.getFrequency_id().getId()
        );
    }
}
