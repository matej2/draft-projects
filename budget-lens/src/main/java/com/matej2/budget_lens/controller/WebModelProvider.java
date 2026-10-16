package com.matej2.budget_lens.controller;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.domain.dto.response.*;
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
import java.util.List;
import java.util.Map;

import static com.matej2.budget_lens.utils.InsightUtils.*;

@Component
@RequiredArgsConstructor
public class WebModelProvider {
    private final FrequencyService frequencyService;
    private final CategoryService categoryService;
    private final InsightService insightService;
    private final BudgetService budgetService;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return !(authentication instanceof AnonymousAuthenticationToken);
    }

    private Map<String, Object> getPageUrlMapping() {
        return Map.of(
                "Home", "/"
        );
    }

    private Map<String, Object> getInitialAttributes(String returnUrl, String pageTitle) {
        return Map.of(
                "returnUrl", returnUrl,
                "isAuthenticated", isAuthenticated(),
                "pageTitle", pageTitle,
                "pageUrlMapping",  getPageUrlMapping()
        );
    }


    public @NonNull Map<String, Object> getAttributesForExpenses(Integer pageNumber, List<ExpenseResponse> expenses) {
        Map<YearMonth, List<InsightsResponse>> groupedAndFormattedInsights = getGroupedInsights(
                getFormatedInsights(insightService.getAllInsights())
        );

        Map<String, Object> modelAttributes = new HashMap<>(getInitialAttributes("/", "Main"));
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

    public Map<String, Object> getAttributesForBudget(List<CategoryInsightsResponse>  categoryInsights, Map<FrequencyResponse, Integer> frequencyToBudget,  String yearMonth, CategoryResponse categoryResponse ) {
        String pageTitle = String.format("%s budgets", categoryResponse.name());
        Map<String, Object> modelAttributes = new HashMap<>(getInitialAttributes("/", pageTitle));
        modelAttributes.putAll(Map.of(
                "insights", getFormatedInsights(categoryInsights),
                "budgets", getFormattedBudgetInsight(categoryInsights, frequencyToBudget),
                "yearMonth", yearMonth,
                "isAuthenticated", isAuthenticated(),
                "frequencies", frequencyService.getAllFrequencies(),
                "categories", List.of(categoryResponse),
                "budgetRequest", new BudgetRequest(null, null, null, null)
        ));
        return modelAttributes;
    }
}
