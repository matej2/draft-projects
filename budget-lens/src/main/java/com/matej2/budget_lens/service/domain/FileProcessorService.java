package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.mapper.ExpenseMapper;
import com.matej2.budget_lens.model.csv.CSVImportRow;
import com.matej2.budget_lens.model.entity.Category;
import com.matej2.budget_lens.model.entity.Expense;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import com.matej2.budget_lens.utils.CsvUtils;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.matej2.budget_lens.utils.CsvUtils.getCsvReader;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileProcessorService {
    private final CategoryService categoryService;
    private final ExpenseMapper expenseMapper;
    private final ExpenseRepository expenseRepository;

    public void saveExpenseListFromFile(MultipartFile file) throws IOException {
        if (CsvUtils.hasCSVFormat(file)) {

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
