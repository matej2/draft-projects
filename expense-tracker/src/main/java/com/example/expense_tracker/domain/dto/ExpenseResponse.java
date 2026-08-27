package com.example.expense_tracker.domain.dto;

import java.time.LocalDate;

public record ExpenseResponse(
    Integer id,
    String note,
    Integer cost,
    LocalDate expenseDate,
    Integer frequency,
    Integer totalCost,
    Integer owner
){}
