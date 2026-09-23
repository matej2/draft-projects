package com.matej2.expense_tracker.domain.jpa;

import com.matej2.expense_tracker.utils.CsvUtils;

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
                CsvUtils.roundToTwoDecimals(stdDev),
                CsvUtils.roundToTwoDecimals(stdDevPercent),
                CsvUtils.roundToTwoDecimals(avg)
        );
    }
}
