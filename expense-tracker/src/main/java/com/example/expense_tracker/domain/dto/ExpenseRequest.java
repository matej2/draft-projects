package com.example.expense_tracker.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ExpenseRequest(
   String note,
   @NotNull
   @Positive
   Integer cost,
   @NotNull
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
   LocalDate expenseDate,
   @NotNull
   Integer frequencyId,
   @NotNull
   Integer categoryId,
   Integer ownerId
) {}
