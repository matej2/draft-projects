package com.example.expense_tracker.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Table(name = "expense")
@Data
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String note;
    private Integer cost;
    private LocalDate expenseDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frequency", nullable = false)
    @JsonIgnoreProperties("expenseList")
    private Frequency frequency;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", nullable = false)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private User owner;
}
