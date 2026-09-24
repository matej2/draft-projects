package com.matej2.expense_tracker.service.domain;

import com.matej2.expense_tracker.domain.dto.CurrentCategoryBudgetResponse;
import com.matej2.expense_tracker.domain.dto.ExpenseFilterRequest;
import com.matej2.expense_tracker.domain.dto.ExpenseRequest;
import com.matej2.expense_tracker.domain.dto.ExpenseResponse;
import com.matej2.expense_tracker.domain.dto.csv.CSVImportRow;
import com.matej2.expense_tracker.domain.entity.*;
import com.matej2.expense_tracker.domain.jpa.CategoryInsightResultRow;
import com.matej2.expense_tracker.domain.mapper.ExpenseMapper;
import com.matej2.expense_tracker.repository.domain.BudgetRepository;
import com.matej2.expense_tracker.repository.domain.ExpenseRepository;
import com.matej2.expense_tracker.utils.CSVHelper;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.matej2.expense_tracker.utils.CsvUtils.getCsvReader;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseTrackingService {
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;


    private final FrequencyService  frequencyService;
    private final CategoryService  categoryService;

    public synchronized void addExpense(ExpenseRequest expense){
        Frequency frequency = this.frequencyService.getFrequencyOrThrow(expense.frequencyId());
        Category category = this.categoryService.getCategory(expense.categoryId());

        Expense mappedExpense = ExpenseMapper.fromExpenseRequest(expense);
        mappedExpense.setFrequency(frequency);
        mappedExpense.setCategory(category);

        Authentication authenticationContext  =  SecurityContextHolder.getContext().getAuthentication();
        if (authenticationContext != null && authenticationContext.getPrincipal() instanceof User authenticatedUser) {
            mappedExpense.setOwner(authenticatedUser);
        }

        this.expenseRepository.save(mappedExpense);
    }

    public synchronized List<ExpenseResponse> getExpense(Pageable pageable) {
        return this.expenseRepository.findAll(pageable).stream()
                .map(ExpenseMapper::toExpenseResponse)
                .toList();
    }

    // In real world scenario I would use mapper that would update only defined properties
    // For simplicity purposes I override whole record
    public synchronized void updateExpense(Integer id, ExpenseRequest expenseRequest) {
        Expense mappedExpense = ExpenseMapper.fromExpenseRequest(expenseRequest);

        mappedExpense.setId(id);
        mappedExpense.setFrequency(this.frequencyService.getFrequencyOrThrow(expenseRequest.frequencyId()));

        this.expenseRepository.save(mappedExpense);
    }


    public List<ExpenseResponse> getExpenseByDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<Expense> filteredExpense = this.expenseRepository.findByExpenseDateBetween(startDate, endDate, pageable);

        return filteredExpense.stream().map(ExpenseMapper::toExpenseResponse).toList();
    }

    public synchronized void deleteExpense(Integer id) {
        expenseRepository.deleteById(id);
    }

    public CategoryInsightResultRow insightsByCategory(final ExpenseFilterRequest expenseFilter, Integer categoryId) {
        return expenseRepository.insightsByCategoryIdByDateBetween(
                categoryId,
                expenseFilter.startDate(),
                expenseFilter.endDate()
        );
    }


    public List<CurrentCategoryBudgetResponse> getBudgetStatus(final ExpenseFilterRequest expenseFilter) {
        List<Budget> budgetlist = this.budgetRepository.findAll();

        return budgetlist.stream().map(
                b -> {
                    Float currentSum = this.expenseRepository.summarizeCurrentAmountByCategoryIdByDateBetween(
                            b.getCategory().getId(),
                            expenseFilter.startDate(),
                            expenseFilter.endDate());
                    currentSum = currentSum != null ? currentSum : 0f;

                    return new CurrentCategoryBudgetResponse(
                            b.getCategory().getName(),
                            this.budgetRepository.findOneByCategory(b.getCategory()).getMonthlyLimit(),
                            currentSum);
                }
        ).toList();
    }

    public void saveFromFile(MultipartFile file) throws IOException {
        log.info("File uploaded successfully: file name {}, file size {}", file.getName(), file.getSize());

        if (CSVHelper.hasCSVFormat(file)) {

            List<CSVImportRow> result;

            try(CSVReader reader = getCsvReader(file.getInputStream())) {
                 result = new CsvToBeanBuilder<CSVImportRow>(reader)
                        .withType(CSVImportRow.class)
                        .build()
                        .parse();
            }

            List<Expense> expenseList = result.stream().map(ExpenseMapper::toExpense).toList();

            this.expenseRepository.saveAll(expenseList);
        }
    }
}
