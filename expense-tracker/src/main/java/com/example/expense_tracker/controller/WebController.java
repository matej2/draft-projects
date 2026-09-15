package com.example.expense_tracker.controller;

import com.example.expense_tracker.domain.dto.CategoryResponse;
import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.dto.FrequencyResponse;
import com.example.expense_tracker.service.CategoryService;
import com.example.expense_tracker.service.ExpenseTrackingService;
import com.example.expense_tracker.service.FrequencyService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String home(Model model, Integer pageNumber) {
        pageNumber = pageNumber == null ? 0 : pageNumber;
        Pageable sortedByDate =
                PageRequest.of(pageNumber, 40, Sort.by("expenseDate").descending());

        List<ExpenseResponse> expenses = expenseTrackingService.getExpense(sortedByDate);
        List<FrequencyResponse> frequencies = frequencyService.getFrequency();
        List<CategoryResponse> categories = categoryService.getAllCategories();

        model.addAttribute("pageNumber", pageNumber);

        Map<String, List<? extends Record>> attributes = Map.of(
                "expenses", expenses,
                "frequencies", frequencies,
                "categories", categories
        );

        model.addAllAttributes(attributes);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean isAuthenticated = true;

        if (authentication instanceof AnonymousAuthenticationToken) {
            isAuthenticated = false;
        }
        model.addAttribute("isAuthenticated", isAuthenticated);

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

    @DeleteMapping("/deleteExpense/{id}")
    public String deleteExpense(@PathVariable Integer id){
        this.expenseTrackingService.deleteExpense(id);
        return "redirect:/";
    }

}
