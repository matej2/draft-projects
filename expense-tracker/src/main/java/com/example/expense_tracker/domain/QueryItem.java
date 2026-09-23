package com.example.expense_tracker.domain;

public record QueryItem(
        String code,
        Selection selection
) {}