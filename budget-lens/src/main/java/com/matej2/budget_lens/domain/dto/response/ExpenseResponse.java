package com.matej2.budget_lens.domain.dto.response;

import java.time.LocalDate;

public record ExpenseResponse(
    Integer id,
    String note,
    Float cost,
    LocalDate expenseDate,
    Integer frequency,
    Integer category,
    Float totalCost,
    Integer owner
){}
