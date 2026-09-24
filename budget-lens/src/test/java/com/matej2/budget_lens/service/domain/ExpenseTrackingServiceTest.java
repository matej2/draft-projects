package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.CurrentCategoryBudgetResponse;
import com.matej2.budget_lens.domain.dto.ExpenseFilterRequest;
import com.matej2.budget_lens.domain.dto.ExpenseRequest;
import com.matej2.budget_lens.domain.entity.Budget;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Expense;
import com.matej2.budget_lens.domain.entity.User;
import com.matej2.budget_lens.repository.domain.BudgetRepository;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseTrackingServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private ExpenseTrackingService expenseTrackingService;

    @Captor
    ArgumentCaptor<Expense> expenseRepositoryCaptor;


    private void setSecurityContext(User authenticatedUser) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAddExpense() {
        ExpenseRequest expense = new ExpenseRequest(
            "test",
            12.0f,
            LocalDate.now(),
            0,
            0,
            0
        );
        User authenticatedUser = new User();
        setSecurityContext(authenticatedUser);

        expenseTrackingService.addExpense(expense);

        verify(expenseRepository).save(expenseRepositoryCaptor.capture());
        Expense expenseCapture = expenseRepositoryCaptor.getValue();
        assertThat(expenseCapture).isNotNull();
    }

    @Test
    void testGetBudgetStatus() {
        // Request filter
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now();
        ExpenseFilterRequest filter = new ExpenseFilterRequest(startDate, endDate);

        // Budget data
        Category category = new Category();
        category.setId(0);
        category.setName("test");

        Budget budget = new  Budget();
        budget.setId(1);
        budget.setCategory(category);
        budget.setMonthlyLimit(50.0f);

        when(budgetRepository.findAll()).thenReturn(List.of(budget));
        when(budgetRepository.findOneByCategory(any(Category.class))).thenReturn(budget);
        when(expenseRepository.summarizeCurrentAmountByCategoryIdByDateBetween(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(35.0f);

        List<CurrentCategoryBudgetResponse> response = this.expenseTrackingService.getBudgetStatus(filter);

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);

        CurrentCategoryBudgetResponse currentCategoryBudgetResponse = response.getFirst();
        assertThat(currentCategoryBudgetResponse.category()).isEqualTo("test");
        assertThat(currentCategoryBudgetResponse.monthlyLimit()).isEqualTo(50.0f);
        assertThat(currentCategoryBudgetResponse.currentAmount()).isEqualTo(35.0f);

    }
}
