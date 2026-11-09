package com.matej2.budget_lens.controller;

import com.matej2.budget_lens.mapper.InsightMapper;
import com.matej2.budget_lens.model.request.BudgetRequest;
import com.matej2.budget_lens.model.response.*;
import com.matej2.budget_lens.service.domain.BudgetService;
import com.matej2.budget_lens.service.domain.CategoryService;
import com.matej2.budget_lens.service.domain.FrequencyService;
import com.matej2.budget_lens.service.domain.InsightService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WebModelProvider {
    private final FrequencyService frequencyService;
    private final CategoryService categoryService;
    private final InsightService insightService;
    private final BudgetService budgetService;

    private final InsightMapper insightMapper;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return !(authentication instanceof AnonymousAuthenticationToken);
    }

    private Map<String, Object> getInitialAttributes(String pageTitle) {
        return Map.of(
                "isAuthenticated", isAuthenticated(),
                "pageTitle", pageTitle
        );
    }

    private List<InsightsResponse> mapToInsightResponseList(List<CategoryInsightsResponse> categoryInsightsResponses) {
        return categoryInsightsResponses.stream()
                .map(insightMapper::toResponse)
                .toList();
    }

    private List<InsightBudgetResponse> mapToInsightBudgetResponse(List<CategoryInsightsResponse> categoryInsights, Map<FrequencyResponse, Integer> frequencyToBudget) {
        return categoryInsights.stream().map(
                insight -> InsightMapper.toResponse(insight, frequencyToBudget)
        ).toList();
    }

    private static Map<YearMonth, List<InsightsResponse>> groupInsightsByDate(List<InsightsResponse> insightList) {
        return insightList.stream()
                .collect(Collectors.groupingBy(
                        InsightsResponse::yearMonth,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    public @NonNull Map<String, Object> getAttributesForExpenses(Integer pageNumber, List<ExpenseResponse> expenses) {
        Map<YearMonth, List<InsightsResponse>> groupedAndFormattedInsights = groupInsightsByDate(
                mapToInsightResponseList(insightService.getAllInsights())
        );

        Map<String, Object> modelAttributes = new HashMap<>(getInitialAttributes("Main"));
        modelAttributes.putAll(Map.of(
                "expenses", expenses,
                "frequencies", frequencyService.getAllFrequencies(),
                "categories", categoryService.getAllCategories(),
                "insights", groupedAndFormattedInsights,
                "budgets", budgetService.getAll(),
                "pageNumber", pageNumber,
                "budgetRequest", new BudgetRequest(null, null, null, null)
        ));

        return modelAttributes;
    }

    public Map<String, Object> getAttributesForBudget(List<CategoryInsightsResponse>  categoryInsights, Map<FrequencyResponse, Integer> frequencyToBudget, String yearMonth, CategoryResponse categoryResponse ) {
        String pageTitle = String.format("%s budgets", categoryResponse.name());
        Map<String, Object> modelAttributes = new HashMap<>(getInitialAttributes(pageTitle));
        modelAttributes.putAll(Map.of(
                "insights", mapToInsightResponseList(categoryInsights),
                "budgets", mapToInsightBudgetResponse(categoryInsights, frequencyToBudget),
                "yearMonth", yearMonth,
                "isAuthenticated", isAuthenticated(),
                "frequencies", frequencyService.getAllFrequencies(),
                "categories", List.of(categoryResponse),
                "budgetRequest", new BudgetRequest(null, null, null, null)
        ));
        return modelAttributes;
    }
}
