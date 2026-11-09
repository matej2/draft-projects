package com.matej2.budget_lens.model.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CategoryInsightResultRow {
    private double stdDevPercent;
    private BigDecimal avg;
    private BigDecimal median;
    private BigDecimal percentile90;
    private Long expenseCount;

    public CategoryInsightResultRow(
            Object a,
            Object avg,
            Object median,
            Object percentile90,
            Object expenseCount
    ) {
        System.out.println("");
    }
}
