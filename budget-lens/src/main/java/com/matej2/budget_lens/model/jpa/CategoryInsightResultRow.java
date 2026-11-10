package com.matej2.budget_lens.model.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CategoryInsightResultRow {
    private double stdDevPercent;
    private Double avg;
    private BigDecimal median;
    private BigDecimal percentile90;
    private Long expenseCount;
}
