package com.example.expense_tracker.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "expense")
@Data
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    String note;
    Integer cost;
    LocalDate expenseDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frequency_id", nullable = false)
    @JsonIgnoreProperties("expenseList")
    public Frequency frequency_id;

    public Expense(String note, Integer cost, LocalDate date, Frequency frequency) {
        this.note = note;
        this.cost = cost;
        this.expenseDate = date;
        this.frequency_id = frequency;
    }
}
