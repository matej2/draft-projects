package com.matej2.budget_lens.domain.dto.response;

import lombok.With;

import java.time.LocalDate;

@With
public record ExpenseResponse(
    Integer id,
    String note,
    Float cost,
    LocalDate expenseDate,
    Integer frequency,
    Integer category,
    Integer owner
){}
