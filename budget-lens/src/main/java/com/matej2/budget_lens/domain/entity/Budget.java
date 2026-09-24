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
    private Float monthlyLimit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", nullable = false)
    private Category category;
}
