package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.request.ExpenseFilterRequest;
import com.matej2.budget_lens.domain.dto.request.ExpenseRequest;
import com.matej2.budget_lens.domain.dto.response.ExpenseResponse;
import com.matej2.budget_lens.domain.dto.csv.CSVImportRow;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Expense;
import com.matej2.budget_lens.domain.entity.Frequency;
import com.matej2.budget_lens.domain.entity.User;
import com.matej2.budget_lens.domain.jpa.CategoryInsightResultRow;
import com.matej2.budget_lens.domain.mapper.ExpenseMapper;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import com.matej2.budget_lens.utils.CSVHelper;
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

import static com.matej2.budget_lens.utils.CsvUtils.getCsvReader;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseTrackingService {
    private final ExpenseRepository expenseRepository;

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

            Category uncategorizedCategory = this.categoryService.getUncategorizedCategory();

            List<Expense> expenseList = result.stream().map(r-> ExpenseMapper.toExpense(r, uncategorizedCategory)).toList();

            this.expenseRepository.saveAll(expenseList);
        }
    }
}
