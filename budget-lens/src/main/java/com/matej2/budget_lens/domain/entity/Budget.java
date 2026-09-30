package com.matej2.budget_lens.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "budget")
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String note;
    private Float quota;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", nullable = false)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frequency", nullable = false)
    private Frequency frequency;
}
