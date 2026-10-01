package com.matej2.budget_lens.controller;

import com.matej2.budget_lens.domain.dto.BudgetRequest;
import com.matej2.budget_lens.domain.dto.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.dto.ExpenseResponse;
import com.matej2.budget_lens.exception.CSVParsingException;
import com.matej2.budget_lens.exception.RecordOverLimitExeption;
import com.matej2.budget_lens.service.domain.*;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ExpenseTrackingService expenseTrackingService;
    private final FrequencyService frequencyService;
    private final CategoryService categoryService;
    private final InsightService insightService;
    private final BudgetService budgetService;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return !(authentication instanceof AnonymousAuthenticationToken);
    }

    @GetMapping
    public String home(Model model, Integer pageNumber) {
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 0;
        }
        Pageable sortedByDate = PageRequest.of(
                pageNumber,
                40,
                Sort.by("expenseDate").descending());
        List<ExpenseResponse> expenses = expenseTrackingService.getExpense(sortedByDate);

        Map<String, Object> attributes = getAttributesForExpenses(pageNumber, expenses);
        model.addAllAttributes(attributes);

        return "index";
    }

    private Map<YearMonth, List<CategoryInsightsResponse>> getGroupedInsights(List<CategoryInsightsResponse> insightList) {
        return insightList.stream()
                .collect(Collectors.groupingBy(
                        CategoryInsightsResponse::yearMonth,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    private @NonNull Map<String, Object> getAttributesForExpenses(Integer pageNumber, List<ExpenseResponse> expenses) {
        return Map.of(
                "expenses", expenses,
                "frequencies", frequencyService.getAllFrequencies(),
                "categories", categoryService.getAllCategories(),
                "insights", getGroupedInsights(insightService.getAllInsights()),
                "budgets", budgetService.getAll(),
                "pageNumber", pageNumber,
                "isAuthenticated", isAuthenticated(),
                "budgetRequest", new BudgetRequest(null, null, null, null)
        );
    }

    @PostMapping("/submitExpense")
    public String submitExpense(
            @ModelAttribute BudgetRequest budgetRequest,
            RedirectAttributes redirectAttributes
    ) {
        // Public endpoint with rate limit
        try {
            this.budgetService.add(budgetRequest);
        } catch (RecordOverLimitExeption e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @DeleteMapping("/deleteExpense/{id}")
    public String deleteExpense(@PathVariable Integer id){
        this.expenseTrackingService.deleteExpense(id);
        return "redirect:/";
    }

    @PostMapping("/uploadCsv")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            this.expenseTrackingService.saveFromFile(file);
        } catch (IOException e) {
            throw new CSVParsingException(e.getMessage());
        }
        return "redirect:/";
    }

}
