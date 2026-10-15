package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.domain.dto.response.BudgetResponse;
import com.matej2.budget_lens.domain.entity.Budget;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Frequency;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper implements Mapper<BudgetRequest, BudgetResponse, Budget> {

    public Budget toEntity(BudgetRequest dto) {
        Budget budgetEntity = new Budget();
        budgetEntity.setNote(dto.note());
        budgetEntity.setQuota(dto.quota());
        return budgetEntity;
    }

    public Budget add(Budget budget, Category category, Frequency frequency) {
        budget.setCategory(category);
        budget.setFrequency(frequency);
        return budget;
    }

    public BudgetResponse toResponse(Budget entity) {
        return new BudgetResponse(
                entity.getNote(),
                entity.getQuota(),
                entity.getCategory().getId(),
                entity.getFrequency().getId()
        );
    }
}
