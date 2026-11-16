package com.matej2.budget_lens.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@AllArgsConstructor
@Getter
public class CategoryInsightsResponse {
        Integer id;
        YearMonth yearMonth;
        CategoryResponse category;
        Double standardDeviationPercent;
        BigDecimal median;
        BigDecimal percentile90;
        Long expenseCount;
        LocalDate updatedAt;
        Double safeBudgetConfidence;
        Double budgetUtilization;
        @Setter
        Map<FrequencyResponse, BigDecimal> frequencyToBudget;
}
