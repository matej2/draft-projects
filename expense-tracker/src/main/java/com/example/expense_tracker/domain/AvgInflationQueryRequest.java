package com.example.expense_tracker.domain;

import java.util.Arrays;
import java.util.List;

public class AvgInflationQueryRequest {
    private List<QueryItem> query;
    private ResponseFormat response;

    public AvgInflationQueryRequest(List<String> months) {
        this.query = Arrays.asList(
                new QueryItem("MESEC", months),
                new QueryItem("INDEKS", Arrays.asList("2", "3"))
        );
        this.response = new ResponseFormat("json-stat");
    }
}