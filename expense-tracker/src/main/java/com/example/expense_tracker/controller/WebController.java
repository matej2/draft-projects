package com.example.expense_tracker.controller;

import com.example.expense_tracker.domain.dto.CategoryResponse;
import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.dto.FrequencyResponse;
import com.example.expense_tracker.service.CategoryService;
import com.example.expense_tracker.service.ExpenseTrackingService;
import com.example.expense_tracker.service.FrequencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ExpenseTrackingService expenseTrackingService;
    private final FrequencyService frequencyService;
    private final CategoryService categoryService;

    @GetMapping
    public String home(Model model) {
        List<ExpenseResponse> expenses = expenseTrackingService.getExpense();
        List<FrequencyResponse> frequencies = frequencyService.getFrequency();
        List<CategoryResponse> categories = categoryService.getAllCategories();

        Map<String, List<? extends Record>> attributes = Map.of(
                "expenses", expenses,
                "frequencies", frequencies,
                "categories", categories
        );

        model.addAllAttributes(attributes);

        model.addAttribute("expenseRequest",new ExpenseRequest(null, null, null, null, null, null));
        return "index";
    }

    @PostMapping("/submitExpense")
    public String submitExpense(
            @ModelAttribute ExpenseRequest expenseRequest
    ) {
        // Public endpoint with rate limit
        this.expenseTrackingService.addExpense(expenseRequest);
        return "redirect:/";
    }
}
