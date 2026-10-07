package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.domain.entity.Budget;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Frequency;
import com.matej2.budget_lens.domain.mapper.BudgetMapper;
import com.matej2.budget_lens.repository.domain.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final CategoryService categoryService;
    private final FrequencyService frequencyService;

    public void add(BudgetRequest budget) {
        Category categoryEntity = categoryService.getCategory(budget.categoryId());
        Frequency frequency = frequencyService.getFrequencyOrThrow(budget.frequencyId());

        Budget budgetEntity = budgetMapper.toEntity(budget,  categoryEntity, frequency);
        this.budgetRepository.save(budgetEntity);
    }

    /// TODO: Replace with dto class
    public List<BudgetRequest> getAll() {
        return this.budgetRepository.findAll().stream()
                .map(budgetMapper::toResponse)
                .toList();
    }

    public List<BudgetRequest> getAllByCategoryId(Integer categoryId) {
        return budgetRepository
                .findAllByCategoryId(categoryId).stream()
                .map(budgetMapper::toResponse)
                .toList();
    }
}
