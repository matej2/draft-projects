package com.matej2.budget_lens.domain.dto;

import java.time.LocalDate;

public record ExpenseResponse(
    Integer id,
    String note,
    Float cost,
    LocalDate expenseDate,
    Integer frequency,
    Float totalCost,
    Integer owner
){}
