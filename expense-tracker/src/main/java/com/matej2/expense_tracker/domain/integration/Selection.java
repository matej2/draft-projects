package com.matej2.expense_tracker.domain.integration;

import java.util.List;

public record Selection(
        String filter,
        List<String> values
) {}