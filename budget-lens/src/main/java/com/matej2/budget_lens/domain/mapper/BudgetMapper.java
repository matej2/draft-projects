package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.domain.entity.Budget;
import com.matej2.budget_lens.service.domain.CategoryService;
import com.matej2.budget_lens.service.domain.FrequencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetMapper implements Mapper<BudgetRequest, Budget> {
    private final CategoryService categoryService;
    private final FrequencyService frequencyService;

    public Budget toEntity(BudgetRequest dto) {
        Budget budgetEntity = new Budget();
        budgetEntity.setNote(dto.note());
        budgetEntity.setQuota(dto.quota());
        budgetEntity.setCategory(categoryService.getCategory(dto.categoryId()));
        budgetEntity.setFrequency(frequencyService.getFrequencyOrThrow(dto.frequencyId()));
        return budgetEntity;
    }

    public BudgetRequest toDto(Budget entity) {
        return new BudgetRequest(
                entity.getNote(),
                entity.getQuota(),
                entity.getCategory().getId(),
                entity.getFrequency().getId()
        );
    }
}
