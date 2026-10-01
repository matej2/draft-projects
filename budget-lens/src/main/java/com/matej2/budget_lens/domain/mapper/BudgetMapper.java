package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.domain.entity.Budget;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Frequency;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper implements Mapper<BudgetRequest, BudgetRequest, Budget> {

    public Budget toEntity(BudgetRequest dto, Category category, Frequency frequency) {
        Budget budgetEntity = new Budget();
        budgetEntity.setNote(dto.note());
        budgetEntity.setQuota(dto.quota());
        budgetEntity.setCategory(category);
        budgetEntity.setFrequency(frequency);
        return budgetEntity;
    }

    public Budget toEntity(BudgetRequest dto) {
        throw new  UnsupportedOperationException("Not supported yet.");
    }

    public BudgetRequest toResponse(Budget entity) {
        return new BudgetRequest(
                entity.getNote(),
                entity.getQuota(),
                entity.getCategory().getId(),
                entity.getFrequency().getId()
        );
    }
}
