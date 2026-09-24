package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.ExpenseResponse;
import com.matej2.budget_lens.domain.dto.FrequencyResponse;
import com.matej2.budget_lens.domain.entity.Frequency;

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
