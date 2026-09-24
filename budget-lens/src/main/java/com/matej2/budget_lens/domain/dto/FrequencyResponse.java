package com.matej2.budget_lens.domain.dto;

import java.util.List;

public record FrequencyResponse(
        Integer id,
        short number,
        String description,
        List<ExpenseResponse> expenses
){}
