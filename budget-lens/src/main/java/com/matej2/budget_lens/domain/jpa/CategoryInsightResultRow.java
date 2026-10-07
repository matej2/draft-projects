package com.matej2.budget_lens.domain.jpa;

import lombok.Data;

@Data
public class CategoryInsightResultRow {
    private double stdDevPercent;
    private double avg;
    private double median;
    private double percentile90;
    private Long expenseCount;

    public CategoryInsightResultRow(
            Double stdDevPercent,
            Double avg,
            Float median,
            Float percentile90,
            Long expenseCount
    ) {
                this.stdDevPercent = stdDevPercent;
                this.avg = avg;
                this.median = median;
                this.percentile90 = percentile90;
                this.expenseCount = expenseCount;
    }
}
