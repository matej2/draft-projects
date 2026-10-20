package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.csv.CSVImportRow;
import com.matej2.budget_lens.domain.dto.request.ExpenseFilterRequest;
import com.matej2.budget_lens.domain.dto.request.ExpenseRequest;
import com.matej2.budget_lens.domain.dto.response.ExpenseResponse;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Expense;
import com.matej2.budget_lens.domain.entity.User;
import com.matej2.budget_lens.domain.jpa.CategoryInsightResultRow;
import com.matej2.budget_lens.domain.mapper.ExpenseMapper;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
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
    private final ExpenseMapper expenseMapper;

    private final CategoryService  categoryService;
    private final CategoryRepository  categoryRepository;

    public synchronized void addExpense(ExpenseRequest expense){
        Category categoryEntity = categoryRepository.findById(expense.categoryId()).orElseThrow();

        Expense mappedExpense = expenseMapper.toEntity(expense);
        mappedExpense.setCategory(categoryEntity);

        Authentication authenticationContext  =  SecurityContextHolder.getContext().getAuthentication();
        if (authenticationContext != null && authenticationContext.getPrincipal() instanceof User authenticatedUser) {
            mappedExpense.setOwner(authenticatedUser);
        }

        this.expenseRepository.save(mappedExpense);

        log.info("Saved expense: {}, category: {}", mappedExpense.getNote(), mappedExpense.getCategory());
    }

    public synchronized List<ExpenseResponse> getExpense(Pageable pageable) {
        return this.expenseRepository.findAll(pageable).stream()
                .map(expenseMapper::toResponse)
                .toList();
    }

    // In real world scenario I would use mapper that would update only defined properties
    // For simplicity purposes I override whole record
    public synchronized void updateExpense(Integer id, ExpenseRequest expenseRequest) {
        Expense mappedExpense = expenseMapper.toEntity(expenseRequest);

        mappedExpense.setId(id);

        this.expenseRepository.save(mappedExpense);
        log.info("Updated expense: {}, category: {}", mappedExpense.getNote(), mappedExpense.getCategory());
    }

    public List<ExpenseResponse> getExpenseByDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<Expense> filteredExpense = this.expenseRepository.findByExpenseDateBetween(startDate, endDate, pageable).getContent();

        return filteredExpense.stream().map(expenseMapper::toResponse).toList();
    }

    public ExpenseResponse getExpenseById(Integer id) {
        return this.expenseRepository.findById(id).stream().map(expenseMapper::toResponse).findFirst().orElseThrow();
    }

    public synchronized void deleteExpense(Integer id) {
        expenseRepository.deleteById(id);
        log.info("Delete expense with id: {}", id);
    }

    public CategoryInsightResultRow insightsByCategory(final ExpenseFilterRequest expenseFilter, Integer categoryId) {
        return expenseRepository.insightsByCategoryIdByDateBetween(
                categoryId,
                expenseFilter.startDate(),
                expenseFilter.endDate()
        );
    }


    public void saveFromFile(MultipartFile file) throws IOException {
        if (CSVHelper.hasCSVFormat(file)) {

            List<CSVImportRow> result;

            try(CSVReader reader = getCsvReader(file.getInputStream())) {
                 result = new CsvToBeanBuilder<CSVImportRow>(reader)
                        .withType(CSVImportRow.class)
                        .build()
                        .parse();
            }

            Category uncategorizedCategory = this.categoryService.getUncategorizedCategory();

            List<Expense> expenseList = result.stream().map(r-> expenseMapper.toExpense(r, uncategorizedCategory)).toList();

            this.expenseRepository.saveAll(expenseList);
        }
        log.info("Imported expense data from file: file name {}, file size {}, content type: {}", file.getOriginalFilename(), file.getSize(), file.getContentType());
    }
}
