package com.matej2.budget_lens.domain.jpa;

import com.matej2.budget_lens.utils.MathUtils;
import lombok.Data;

@Data
public class CategoryInsightResultRow {
    private float stdDev;
    private float stdDevPercent;
    private float avg;
    private float median;
    private float percentile90;
    private boolean isFixedCost;
    private Long expenseCount;

    public CategoryInsightResultRow(
            Double stdDev,
            Double stdDevPercent,
            Double avg,
            Float median,
            Float percentile90,
            Boolean isFixedCost,
            Long expenseCount
    ) {
                this.stdDev = MathUtils.toTwoDecimals(stdDev);
                this.stdDevPercent = MathUtils.toTwoDecimals(stdDevPercent);
                this.avg = MathUtils.toTwoDecimals(avg);
                this.median = MathUtils.toTwoDecimals(median.doubleValue());
                this.percentile90 = MathUtils.toTwoDecimals(percentile90.doubleValue());
                this.isFixedCost = isFixedCost;
                this.expenseCount = expenseCount;
    }
}
