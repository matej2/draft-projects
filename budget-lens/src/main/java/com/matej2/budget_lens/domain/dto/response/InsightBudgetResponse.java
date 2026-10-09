package com.matej2.budget_lens.domain.dto.response;


import java.time.LocalDate;
import java.util.Map;

public record InsightBudgetResponse(
        Map<FrequencyResponse, Integer> frequencyToBudget,
        Integer safeBudgetConfidence,
        Integer budgetUtilization,
        LocalDate insightUpdatedAt
) {}