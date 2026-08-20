package com.example.expense_tracker.domain.dto;

import java.time.LocalDate;

public record ExpenseResponse(
    String note,
    Integer cost,
    LocalDate expense_date,
    Integer frequency
){}
