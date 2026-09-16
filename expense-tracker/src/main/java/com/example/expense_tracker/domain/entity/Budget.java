package com.example.expense_tracker.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "budget")
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float monthlyLimit;
    private Byte month;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", nullable = false)
    private Category category;
}
