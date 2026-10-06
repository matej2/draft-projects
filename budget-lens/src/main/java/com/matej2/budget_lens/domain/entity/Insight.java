package com.matej2.budget_lens.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

// Ideally this entity would be handled in a separate database that is more optimized
// Calculates insights for current month
@Entity
@Table(
        name = "insight",
        uniqueConstraints = @UniqueConstraint(
                name = "insight_year_month_category_key",
                columnNames = {"year_month", "category"})
)
@Data
public class Insight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float median;
    private Float percentile90;
    private YearMonth yearMonth;
    private Float standardDeviationPercent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    Category category;
    private Long expenseCount;
    private LocalDate updatedAt;
    private Float safeBudgetConfidence;
}
