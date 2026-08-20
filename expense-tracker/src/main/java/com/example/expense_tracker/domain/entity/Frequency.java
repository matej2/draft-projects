package com.example.expense_tracker.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "frequency")
@Getter
public class Frequency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private short number;
    private String description;
    @OneToMany(mappedBy = "frequency", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("frequency")
    private final List<Expense> expenseList = new ArrayList<>();
}
