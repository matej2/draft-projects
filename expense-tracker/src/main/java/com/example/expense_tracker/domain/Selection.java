package com.example.expense_tracker.domain;

import java.util.List;

public record Selection(
        String filter,
        List<String> values
) {}