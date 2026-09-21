package com.example.expense_tracker.domain.mapper;

import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.dto.csv.CSVImportRow;
import com.example.expense_tracker.domain.entity.Expense;
import com.example.expense_tracker.domain.entity.Frequency;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExpenseMapper {
    public static final String DATE = "date";
    public static final String REF_NUM = "refNum";
    public static final String SUBJECT = "subject";
    public static final String DESCRIPTION = "description";
    public static final String POSITIVE_TRAFFIC = "positiveTraffic";
    public static final String NEGATIVE_TRAFFIC = "negativeTraffic";

    public Expense fromExpenseRequest(ExpenseRequest expenseRequest) {
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
                totalCost,
                1
        );
    }

    public static Expense toExpense(CSVImportRow row) {
        Expense expense = new Expense();
        LocalDate date = LocalDate.parse(row.getDate());
        expense.setExpenseDate(date);
        expense.setNote(String.valueOf(row.getRefNum()));
        expense.setCost(row.getNegativeTraffic());

        return expense;
    }
}
