package com.matej2.budget_lens.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
   String note,
   @NotNull
   @Positive
   BigDecimal cost,
   @NotNull
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
   LocalDate expenseDate,
   @NotNull
   Integer frequencyId,
   @NotNull
   Integer categoryId,
   Integer ownerId
) {}
