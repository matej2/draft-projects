package com.matej2.budget_lens.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Entity
@Table(name = "budget")
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String note;
    @Nonnull
    @PositiveOrZero
    private Float quota;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", nullable = false)
    @Nonnull
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frequency", nullable = false)
    @Nonnull
    private Frequency frequency;
}
