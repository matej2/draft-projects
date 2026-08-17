package com.example.expense_tracker.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    Date date;

    public Expense(String note, Integer cost, Date date) {
        this.note = note;
        this.cost = cost;
        this.date = date;
    }
}
