package com.matej2.budget_lens.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
@NoArgsConstructor
public class Insight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @DecimalMin(value = "0.00")
    private BigDecimal median;
    @DecimalMin(value = "0.00")
    private BigDecimal percentile90;
    @Nonnull
    private YearMonth yearMonth;
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "1.00")
    private Double standardDeviationPercent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    @Nonnull
    Category category;
    @Min(0)
    private Long expenseCount;
    @Nonnull
    private LocalDate updatedAt;
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "1.00")
    private Double safeBudgetConfidence;
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "1.00")
    private Double budgetUtilization;
}
