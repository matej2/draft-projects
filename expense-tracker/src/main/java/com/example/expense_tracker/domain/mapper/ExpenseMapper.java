package com.example.expense_tracker.domain.mapper;

import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.entity.Expense;
import org.apache.commons.csv.CSVRecord;
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
        // Calculate total cost in a year
        Float totalCost = expense.getCost() * expense.getFrequency().getNumber();

        return new ExpenseResponse(
                expense.getId(),
                expense.getNote(),
                expense.getCost(),
                expense.getExpenseDate(),
                expense.getFrequency().getId(),
                totalCost,
                1
        );
    }

    public static Expense toExpense(CSVRecord record) {
        String reference = String.format(
                "Reference: %s, Sender: %s",
                record.get(REF_NUM),
                record.get(SUBJECT)
        );

        Expense expense = new Expense();
        expense.setExpenseDate(LocalDate.parse(record.get(DATE)));
        expense.setNote(reference);
        expense.setCost(Float.parseFloat(record.get(NEGATIVE_TRAFFIC)));
        expense.setExpenseDate(LocalDate.parse(record.get(DATE)));

        return expense;
    }
}
