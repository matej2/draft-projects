package com.matej2.budget_lens.controller;

import com.matej2.budget_lens.domain.dto.ExpenseRequest;
import com.matej2.budget_lens.domain.dto.ExpenseResponse;
import com.matej2.budget_lens.exception.RecordOverLimitExeption;
import com.matej2.budget_lens.service.domain.CategoryService;
import com.matej2.budget_lens.service.domain.ExpenseTrackingService;
import com.matej2.budget_lens.service.domain.FrequencyService;
import com.matej2.budget_lens.service.domain.InsightService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private @NonNull Map<String, Object> getAttributesForExpenses(Integer pageNumber, List<ExpenseResponse> expenses) {
        return Map.of(
                "expenses", expenses,
                "frequencies", frequencyService.getAllFrequencies(),
                "categories", categoryService.getAllCategories(),
                "pageNumber", pageNumber,
                "isAuthenticated", isAuthenticated(),
                "expenseRequest",new ExpenseRequest(null, null, null, null, null, null)
        );
    }

    @PostMapping("/submitExpense")
    public String submitExpense(
            @ModelAttribute ExpenseRequest expenseRequest,
            RedirectAttributes redirectAttributes
    ) {
        // Public endpoint with rate limit
        String error = "";
        try {
            this.expenseTrackingService.addExpense(expenseRequest);
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

}
