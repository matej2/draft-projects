package com.matej2.budget_lens.domain.integration;

import java.util.List;

public record Selection(
        String filter,
        List<String> values
) {}