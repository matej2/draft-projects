package com.example.expense_tracker.controller;

import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.service.CategoryService;
import com.example.expense_tracker.service.ExpenseTrackingService;
import com.example.expense_tracker.service.FrequencyService;
import com.example.expense_tracker.service.InsightService;
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

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ExpenseTrackingService expenseTrackingService;
    private final FrequencyService frequencyService;
    private final CategoryService categoryService;
    private final InsightService insightService;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean isAuthenticated = true;

        if (authentication instanceof AnonymousAuthenticationToken) {
            isAuthenticated = false;
        }
        return isAuthenticated;
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

    private @NonNull Map<String, Object> getAttributesForExpenses(Integer pageNumber, List<ExpenseResponse> expenses) {
        Map<String, Object> attributes = Map.of(
                "expenses", expenses,
                "frequencies", frequencyService.getFrequency(),
                "categories", categoryService.getAllCategories(),
                "pageNumber", pageNumber,
                "isAuthenticated", isAuthenticated(),
                "expenseRequest",new ExpenseRequest(null, null, null, null, null, null)
        );
        return attributes;
    }

    @PostMapping("/submitExpense")
    public String submitExpense(
            @ModelAttribute ExpenseRequest expenseRequest
    ) {
        // Public endpoint with rate limit
        this.expenseTrackingService.addExpense(expenseRequest);
        return "redirect:/";
    }

    @DeleteMapping("/deleteExpense/{id}")
    public String deleteExpense(@PathVariable Integer id){
        this.expenseTrackingService.deleteExpense(id);
        return "redirect:/";
    }

}
