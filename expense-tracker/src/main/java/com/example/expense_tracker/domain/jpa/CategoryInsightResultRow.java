package com.example.expense_tracker.domain.jpa;

import com.example.expense_tracker.domain.Utils;

public record CategoryInsightResultRow (
    float stdDev,
    float stdDevPercent,
    float avg
){
    public CategoryInsightResultRow(
            Double stdDev,
            Double stdDevPercent,
            Double avg
    ) {
        this(
                Utils.roundToTwoDecimals(stdDev),
                Utils.roundToTwoDecimals(stdDevPercent),
                Utils.roundToTwoDecimals(avg)
        );
    }
}
