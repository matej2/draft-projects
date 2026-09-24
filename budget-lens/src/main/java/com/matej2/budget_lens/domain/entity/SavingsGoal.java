package com.matej2.budget_lens.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "savingsgoal")
@Data
public class SavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    Float targetAmount;
    Float currentAmount;
    LocalDate deadline;
}
