package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.csv.CSVImportRow;
import com.matej2.budget_lens.domain.dto.request.ExpenseRequest;
import com.matej2.budget_lens.domain.dto.response.ExpenseResponse;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Expense;
import com.matej2.budget_lens.domain.entity.Frequency;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExpenseMapper implements Mapper<ExpenseRequest, ExpenseResponse, Expense>{

    @Override
    public Expense toEntity(ExpenseRequest expenseRequest) {
        Expense expense = new Expense();
        expense.setNote(expenseRequest.note());
        expense.setCost(expenseRequest.cost());
        expense.setExpenseDate(expenseRequest.expenseDate());

        return expense;
    }

    @Override
    public ExpenseResponse toResponse(Expense expense) {
        Frequency frequency = new Frequency();
        // Calculate total cost in a year

        return new ExpenseResponse(
                expense.getId(),
                expense.getNote(),
                expense.getCost(),
                expense.getExpenseDate(),
                frequency.getId(),
                expense.getCategory() == null ? null: expense.getCategory().getId(),
                1
        );
    }

     public Expense toExpense(CSVImportRow row, Category uncategorized) {
        Expense expense = new Expense();
        LocalDate date = LocalDate.parse(row.getDate());
        expense.setExpenseDate(date);
        expense.setNote(String.valueOf(row.getRefNum()));
        expense.setCost(row.getNegativeTraffic());
        expense.setCategory(uncategorized);

        return expense;
    }
}
