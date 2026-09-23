package com.example.expense_tracker.domain;

import java.util.List;

public record AvgInflationQueryRequest(
        List<QueryItem> query,
        ResponseFormat response
) {}