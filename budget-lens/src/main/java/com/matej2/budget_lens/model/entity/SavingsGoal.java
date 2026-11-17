package com.matej2.budget_lens.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "savingsgoal")
@Data
public class SavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Nonnull
    @NotBlank
    String name;
    @Min(0)
    @Nonnull
    BigDecimal targetAmount;
    BigDecimal currentAmount;
    @Nonnull
    @Future
    LocalDate deadline;
}
