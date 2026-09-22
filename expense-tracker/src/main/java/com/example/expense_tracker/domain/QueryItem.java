package com.example.expense_tracker.domain;

import java.util.List;

class QueryItem {
    public String code;
    public Selection selection;

    public QueryItem(String code, List<String> values) {
        this.code = code;
        this.selection = new Selection("item", values);
    }
}