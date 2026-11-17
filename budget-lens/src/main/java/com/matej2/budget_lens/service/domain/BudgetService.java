package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.mapper.BudgetMapper;
import com.matej2.budget_lens.model.entity.Budget;
import com.matej2.budget_lens.model.request.BudgetRequest;
import com.matej2.budget_lens.model.response.BudgetResponse;
import com.matej2.budget_lens.repository.domain.BudgetRepository;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
import com.matej2.budget_lens.repository.domain.FrequencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper = BudgetMapper.INSTANCE;
    private final CategoryRepository categoryRepository;
    private final FrequencyRepository frequencyRepository;

    @Transactional
    public void save(BudgetRequest budget) {
        categoryRepository.findById(budget.categoryId()).orElseThrow();
        frequencyRepository.findById(budget.frequencyId()).orElseThrow();

        Budget existingBudgetEntity = budgetMapper.budgetRequestToBudget(budget);

        this.budgetRepository.save(existingBudgetEntity);
        log.info("Saved budget {} for category {}", budget.note(), budget.categoryId());
    }

    public List<BudgetResponse> getAll() {
        return this.budgetRepository.findAll().stream()
                .map(BudgetMapper.INSTANCE::budgetToBudgetResponse)
                .toList();
    }

}
