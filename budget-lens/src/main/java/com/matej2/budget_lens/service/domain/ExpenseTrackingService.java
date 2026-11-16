package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.mapper.ExpenseMapper;
import com.matej2.budget_lens.model.entity.Category;
import com.matej2.budget_lens.model.entity.Expense;
import com.matej2.budget_lens.model.integration.AiCategorizationResponse;
import com.matej2.budget_lens.model.request.ExpenseRequest;
import com.matej2.budget_lens.model.response.ExpenseResponse;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseTrackingService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper = ExpenseMapper.INSTANCE;

    private final CategoryRepository  categoryRepository;

    public synchronized void addExpense(ExpenseRequest expense){
        Category categoryEntity = categoryRepository.findById(expense.categoryId()).orElseThrow();

        Expense existingExpense = expenseMapper.expenseRequestToExpense(expense);
        existingExpense.setCategory(categoryEntity);

        this.expenseRepository.save(existingExpense);

        log.info("Saved expense: {}, category: {}", existingExpense.getNote(), existingExpense.getCategory());
    }

    public synchronized List<ExpenseResponse> getExpenseList(Pageable pageable) {
        return this.expenseRepository.findAll(pageable).stream()
                .map(expenseMapper::expenseToExpenseResponse)
                .toList();
    }

    // In real world scenario I would use mapper that would update only defined properties
    // For simplicity purposes I override whole record
    public synchronized void updateExpense(Integer expenseId, ExpenseRequest expenseRequest) {
        Expense mappedExpense = expenseMapper.expenseRequestToExpense(expenseRequest);

        mappedExpense.setId(expenseId);

        this.expenseRepository.save(mappedExpense);
        log.info("Updated expense: {}, category: {}", mappedExpense.getNote(), mappedExpense.getCategory());
    }

    public List<ExpenseResponse> getExpenseByDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<Expense> filteredExpense = this.expenseRepository.findByExpenseDateBetween(startDate, endDate, pageable).getContent();

        return filteredExpense.stream().map(expenseMapper::expenseToExpenseResponse).toList();
    }

    public ExpenseResponse getExpenseById(Integer id) {
        return this.expenseRepository.findById(id).stream().map(expenseMapper::expenseToExpenseResponse).findFirst().orElseThrow();
    }

    public synchronized void deleteExpense(Integer id) {
        expenseRepository.deleteById(id);
        log.info("Delete expense with id: {}", id);
    }

    public Expense applyAiCategorization(AiCategorizationResponse expenseRequest) {
        Category category = categoryRepository.findById(expenseRequest.categoryId()).stream().findFirst().orElseThrow();
        ExpenseResponse existingExpense = this.getExpenseById(expenseRequest.expenseId()).withCategory(category.getId());
        ExpenseResponse updatedExpense = existingExpense.withCategory(category.getId());

        return this.expenseMapper.expenseResponseToExpense(updatedExpense);
    }
}
