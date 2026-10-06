package com.matej2.budget_lens.domain.jpa;

import com.matej2.budget_lens.utils.MathUtils;
import lombok.Data;

@Data
public class CategoryInsightResultRow {
    private float stdDevPercent;
    private float avg;
    private float median;
    private float percentile90;
    private Long expenseCount;

    public CategoryInsightResultRow(
            Double stdDevPercent,
            Double avg,
            Float median,
            Float percentile90,
            Long expenseCount
    ) {
                this.stdDevPercent = MathUtils.toTwoDecimals(stdDevPercent);
                this.avg = MathUtils.toTwoDecimals(avg);
                this.median = MathUtils.toTwoDecimals(median.doubleValue());
                this.percentile90 = MathUtils.toTwoDecimals(percentile90.doubleValue());
                this.expenseCount = expenseCount;
    }
}
