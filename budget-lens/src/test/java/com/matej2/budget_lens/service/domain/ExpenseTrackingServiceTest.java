package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.model.entity.Category;
import com.matej2.budget_lens.model.entity.Expense;
import com.matej2.budget_lens.model.entity.Source;
import com.matej2.budget_lens.model.integration.AiCategorizationResponse;
import com.matej2.budget_lens.model.request.ExpenseRequest;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseTrackingServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseTrackingService expenseTrackingService;

    @Captor
    ArgumentCaptor<Expense> expenseRepositoryCaptor;

    @Test
    void testAddExpense() {
        ExpenseRequest expense = new ExpenseRequest(
            "test",
                BigDecimal.valueOf(12.0f),
            LocalDate.now(),
            0,
            0,
            0
        );

        Category category = new Category();
        category.setName("test");

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));

        expenseTrackingService.addExpense(expense);

        verify(expenseRepository).save(expenseRepositoryCaptor.capture());
        Expense expenseCapture = expenseRepositoryCaptor.getValue();
        assertThat(expenseCapture).isNotNull();
        assertThat(expenseCapture.getNote()).isEqualTo("test");
        assertThat(expenseCapture.getExpenseDate()).isEqualTo(LocalDate.now());
        assertThat(expenseCapture.getCost()).isEqualTo(BigDecimal.valueOf(12.0f));
        assertThat(expenseCapture.getCategory().getName()).isEqualTo("test");

    }

    @Test
    void testUpdateExpense() {
        Integer expenseId;
        ExpenseRequest expenseRequest = new ExpenseRequest(
                "test",
                BigDecimal.valueOf(2.0f),
                LocalDate.now(),
                4,
                1,
                5
        );

        expenseTrackingService.updateExpense(3, expenseRequest);

        verify(expenseRepository).save(expenseRepositoryCaptor.capture());
        Expense expenseCapture = expenseRepositoryCaptor.getValue();

        assertThat(expenseCapture).isNotNull();
    }

    @Test
    void testApplyAiCategorization() {
        AiCategorizationResponse categorizationResponse = new AiCategorizationResponse(4, 6);
        Category category = new Category();
        category.setId(6);
        category.setName("test");

        Source source = new Source();
        source.setSource("testSource");
        source.setCategory(category);

        Expense expense = new Expense();
        expense.setId(4);
        expense.setCost(BigDecimal.valueOf(12.0f));
        expense.setExpenseDate(LocalDate.now());
        expense.setCategory(category);
        expense.setSource(source);

        when(categoryRepository.findById(eq(6))).thenReturn(Optional.of(category));
        when(expenseRepository.findById(eq(4))).thenReturn(Optional.of(expense));

        Expense expenseResponse = expenseTrackingService.applyAiCategorization(categorizationResponse);
        assertThat(expenseResponse).isNotNull();
        assertThat(expenseResponse.getExpenseDate()).isEqualTo(LocalDate.now());
        assertThat(expenseResponse.getCost()).isEqualTo(BigDecimal.valueOf(12.0f));
        assertThat(expenseResponse.getCategory().getId()).isEqualTo(6);
        assertThat(expenseResponse.getId()).isEqualTo(4);
    }
}
