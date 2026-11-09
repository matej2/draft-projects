package com.matej2.budget_lens.model.response;

import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;

@With
public record ExpenseResponse(
    Integer id,
    String note,
    BigDecimal cost,
    LocalDate expenseDate,
    Integer frequency,
    Integer category,
    Integer owner
){}
