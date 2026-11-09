package com.matej2.budget_lens.model.response;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record InsightBudgetResponse(
        Map<FrequencyResponse, BigDecimal> frequencyToBudget,
        Integer safeBudgetConfidence,
        Integer budgetUtilization,
        LocalDate insightUpdatedAt
) {}