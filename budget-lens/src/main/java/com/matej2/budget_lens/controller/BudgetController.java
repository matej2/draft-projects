package com.matej2.budget_lens.controller;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.domain.dto.response.CurrentCategoryBudgetResponse;
import com.matej2.budget_lens.domain.dto.request.ExpenseFilterRequest;
import com.matej2.budget_lens.service.domain.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller(value="/api/budget")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService  budgetService;

    @PostMapping("/status")
    public List<CurrentCategoryBudgetResponse> getBudgetStatus(@RequestBody ExpenseFilterRequest request) {
        return this.budgetService.getBudgetStatus(request);
    }

    @PostMapping("/")
    public void saveBudget(BudgetRequest budgetRequest) {
        this.budgetService.add(budgetRequest);
    }
}
