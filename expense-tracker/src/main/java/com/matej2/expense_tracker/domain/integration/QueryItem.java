package com.matej2.expense_tracker.domain.integration;

public record QueryItem(
        String code,
        Selection selection
) {}