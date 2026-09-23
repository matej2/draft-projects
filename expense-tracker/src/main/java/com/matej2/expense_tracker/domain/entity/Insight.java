package com.matej2.expense_tracker.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

// Ideally this entity would be handled in a separate database that is more optimized
// Calculates insights for current month
@Entity
@Table(name = "insight")
@Data
public class Insight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float average;
    // Standard deviation for last 3 months
    private Float standardDeviation;
    private Float standardDeviationPercent;
    //Category category;
    private Boolean isSentToAI;
}
