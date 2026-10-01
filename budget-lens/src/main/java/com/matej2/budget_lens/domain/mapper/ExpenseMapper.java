package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.request.ExpenseRequest;
import com.matej2.budget_lens.domain.dto.response.ExpenseResponse;
import com.matej2.budget_lens.domain.dto.csv.CSVImportRow;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Expense;
import com.matej2.budget_lens.domain.entity.Frequency;

import java.time.LocalDate;

public class ExpenseMapper {
    public static Expense fromExpenseRequest(ExpenseRequest expenseRequest) {
        Expense expense = new Expense();
        expense.setNote(expenseRequest.note());
        expense.setCost(expenseRequest.cost());
        expense.setExpenseDate(expenseRequest.expenseDate());

        return expense;
    }

    public static ExpenseResponse toExpenseResponse(Expense expense) {
        Frequency frequency = new Frequency();
        // Calculate total cost in a year
        float totalCost = 0f;

        if (expense.getFrequency() != null) {
            frequency = expense.getFrequency();

            if (expense.getCost() != null) {
                totalCost = expense.getCost() * frequency.getNumber();
            }
        }

        return new ExpenseResponse(
                expense.getId(),
                expense.getNote(),
                expense.getCost(),
                expense.getExpenseDate(),
                frequency.getId(),
                expense.getCategory() == null ? null: expense.getCategory().getId(),
                totalCost,
                1
        );
    }

    // TODO: Refactor other mappers into components
    public static Expense toExpense(CSVImportRow row, Category uncategorized) {
        Expense expense = new Expense();
        LocalDate date = LocalDate.parse(row.getDate());
        expense.setExpenseDate(date);
        expense.setNote(String.valueOf(row.getRefNum()));
        expense.setCost(row.getNegativeTraffic());
        expense.setCategory(uncategorized);

        return expense;
    }
}
