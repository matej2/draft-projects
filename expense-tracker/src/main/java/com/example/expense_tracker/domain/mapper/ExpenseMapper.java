package com.example.expense_tracker.domain.mapper;

import com.example.expense_tracker.domain.dto.CSVImportRow;
import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.entity.Expense;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

@Component
public class ExpenseMapper {
    private static final String DATE = "date";
    private static final String REF_NUM = "refNum";
    private static final String SUBJECT = "subject";
    private static final String DESCRIPTION = "description";
    private static final String POSITIVE_TRAFFIC = "positiveTraffic";
    private static final String NEGATIVE_TRAFFIC = "negativeTraffic";

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

    private static CSVImportRow parseSingleRow(CSVRecord record) {
        return new CSVImportRow(
                LocalDate.parse(record.get(DATE)),
                record.get(REF_NUM),
                record.get(SUBJECT),
                record.get(DESCRIPTION),
                Float.parseFloat(record.get(POSITIVE_TRAFFIC)),
                Float.parseFloat(record.get(NEGATIVE_TRAFFIC))
        );
    }

    public static List<CSVImportRow> toCSVImportRow(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        CSVFormat format = CSVFormat.Builder.create().setIgnoreHeaderCase(true).setTrim(true).get();
        CSVParser parser = CSVParser.builder().setReader(reader).setFormat(format).get();

        return parser.getRecords().stream()
                .map(ExpenseMapper::parseSingleRow)
                .toList();
    }
}
