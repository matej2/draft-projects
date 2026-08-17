package com.example.expense_tracker.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "frequency")
@NoArgsConstructor(force = true)
@Getter
public class Frequency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public short number;
    public String description;
    @OneToMany(mappedBy = "frequency", fetch = FetchType.LAZY, orphanRemoval = false)
    @JsonIgnoreProperties("frequency")
    private List<Expense> expenseList = new ArrayList<>();

    public Frequency(short number, String description) {
        this.number = number;
        this.description = description;
    }
}
