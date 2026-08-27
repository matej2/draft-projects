package com.example.expense_tracker.domain.dto;

import java.util.List;

public record FrequencyResponse (
        Integer id,
        short number,
        String description,
        List<ExpenseResponse> expenses
) {}
