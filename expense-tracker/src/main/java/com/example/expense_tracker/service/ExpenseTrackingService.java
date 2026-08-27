package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.dto.ExpenseRequest;
import com.example.expense_tracker.domain.dto.ExpenseResponse;
import com.example.expense_tracker.domain.entity.Category;
import com.example.expense_tracker.domain.entity.Expense;
import com.example.expense_tracker.domain.entity.Frequency;
import com.example.expense_tracker.domain.entity.User;
import com.example.expense_tracker.domain.mapper.ExpenseMapper;
import com.example.expense_tracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseTrackingService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final FrequencyService  frequencyService;
    private final CategoryService  categoryService;
    private final UserDetailService userDetailService;

    public synchronized void addExpense(ExpenseRequest expense){
        Frequency frequency = this.frequencyService.getFrequencyOrThrow(expense.frequencyId());
        Category category = this.categoryService.getCategory(expense.categoryId());

        Expense mappedExpense = this.expenseMapper.fromExpenseRequest(expense);
        mappedExpense.setFrequency(frequency);
        mappedExpense.setCategory(category);

        Authentication authenticationContext  =  SecurityContextHolder.getContext().getAuthentication();
        if (authenticationContext != null && authenticationContext.getPrincipal() instanceof User authenticatedUser) {
            mappedExpense.setOwner(authenticatedUser);
        }

        this.expenseRepository.save(mappedExpense);
    }

    public synchronized List<ExpenseResponse> getExpense() {
        return this.expenseRepository.findAll().stream()
                .map(ExpenseMapper::toExpenseResponse)
                .toList();
    }

    // In real world scenario I would use mapper that would update only defined properties
    // For simplicity purposes I override whole record
    public synchronized void updateExpense(Integer id, ExpenseRequest expenseRequest) {
        Expense mappedExpense = this.expenseMapper.fromExpenseRequest(expenseRequest);

        mappedExpense.setId(id);
        mappedExpense.setFrequency(this.frequencyService.getFrequencyOrThrow(expenseRequest.frequencyId()));

        this.expenseRepository.save(mappedExpense);
    }


    public List<ExpenseResponse> getExpenseByDate(LocalDate startDate, LocalDate endDate) {
        List<Expense> filteredExpense = this.expenseRepository.findByExpenseDateBetween(startDate, endDate);

        return filteredExpense.stream().map(ExpenseMapper::toExpenseResponse).toList();
    }


}
