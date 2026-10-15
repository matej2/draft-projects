package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.domain.dto.response.BudgetResponse;
import com.matej2.budget_lens.domain.entity.Budget;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Frequency;
import com.matej2.budget_lens.domain.mapper.BudgetMapper;
import com.matej2.budget_lens.repository.domain.BudgetRepository;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
import com.matej2.budget_lens.repository.domain.FrequencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final CategoryRepository categoryRepository;
    private final FrequencyRepository frequencyRepository;

    public void add(BudgetRequest budget) {
        Category categoryEntity = categoryRepository.findById(budget.categoryId()).orElseThrow();
        Frequency frequencyEntity = frequencyRepository.findById(budget.frequencyId()).orElseThrow();

        Budget existingBudgetEntity = budgetMapper.toEntity(budget);
        Budget completeBudgetEntity = budgetMapper.add(existingBudgetEntity, categoryEntity, frequencyEntity);

        this.budgetRepository.save(completeBudgetEntity);
    }

    public List<BudgetResponse> getAll() {
        return this.budgetRepository.findAll().stream()
                .map(budgetMapper::toResponse)
                .toList();
    }

}
