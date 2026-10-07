package com.matej2.budget_lens.controller;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.service.domain.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller(value="/api/budget")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService  budgetService;

    @PostMapping("/")
    public void saveBudget(BudgetRequest budgetRequest) {
        this.budgetService.add(budgetRequest);
    }
}
