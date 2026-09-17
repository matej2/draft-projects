package com.example.expense_tracker.domain.dto;

import lombok.ToString;

@ToString
public class CategoryInsightsResponse {
    private String category;
    private Float average;
    private Float stddev;
    private Float currdev;

    public CategoryInsightsResponse(
            String category,
            Float average,
            Float stddev,
            Float currdev
    ) {
        this.category = category;
        this.average = (float) (Math.round(average * 100.0) / 100.0);
        this.stddev = (float) (Math.round(stddev * 100.0) / 100.0);
        this.currdev = (float) (Math.round(currdev * 100.0) / 100.0);
    }

}
