package com.matej2.expense_tracker.domain.mapper;

import com.matej2.expense_tracker.domain.dto.ExpenseResponse;
import com.matej2.expense_tracker.domain.dto.FrequencyResponse;
import com.matej2.expense_tracker.domain.entity.Frequency;

import java.util.List;

public class FrequencyMapper {
    public static FrequencyResponse toResponse(Frequency frequency) {
        List<ExpenseResponse> expenseResponse = frequency.getExpenseList().stream()
                .map(ExpenseMapper::toExpenseResponse)
                .toList();

        return new FrequencyResponse(
                frequency.getId(),
                frequency.getNumber(),
                frequency.getDescription(),
                expenseResponse
        );
    }
}
