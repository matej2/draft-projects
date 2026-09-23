package com.matej2.expense_tracker.domain.integration;

import java.util.List;

public record AvgInflationQueryRequest(
        List<QueryItem> query,
        ResponseFormat response
) {}