package com.matej2.budget_lens.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryInsightResultRow {
    private double stdDevPercent;
    private double avg;
    private double median;
    private double percentile90;
    private Long expenseCount;

}
