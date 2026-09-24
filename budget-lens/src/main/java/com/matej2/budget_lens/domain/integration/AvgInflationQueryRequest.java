package com.matej2.budget_lens.domain.integration;

import java.util.List;

public record AvgInflationQueryRequest(
        List<QueryItem> query,
        ResponseFormat response
) {}