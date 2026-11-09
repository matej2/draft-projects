package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.model.entity.Budget;
import com.matej2.budget_lens.model.request.BudgetRequest;
import com.matej2.budget_lens.model.response.BudgetResponse;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper implements Mapper<BudgetRequest, BudgetResponse, Budget> {

    public Budget toEntity(BudgetRequest dto) {
        Budget budgetEntity = new Budget();
        budgetEntity.setNote(dto.note());
        budgetEntity.setQuota(dto.quota());
        return budgetEntity;
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
