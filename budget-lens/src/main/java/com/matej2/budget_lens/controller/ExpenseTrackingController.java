package com.matej2.budget_lens.controller;

import com.matej2.budget_lens.domain.dto.*;
import com.matej2.budget_lens.domain.dto.exception.ErrorResponse;
import com.matej2.budget_lens.exception.CSVParsingException;
import com.matej2.budget_lens.service.domain.CategoryService;
import com.matej2.budget_lens.service.domain.ExpenseTrackingService;
import com.matej2.budget_lens.service.domain.FrequencyService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
})
@RequiredArgsConstructor
public class ExpenseTrackingController {
    private final ExpenseTrackingService expenseTrackingService;
    private final FrequencyService frequencyService;
    private final CategoryService  categoryService;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;


    @GetMapping("/")
    public String home(Principal principal) {
        return ddlAuto;
    }

    @PostMapping("/expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense found"),
    })
    public List<ExpenseResponse> getExpense(@Nullable @RequestBody ExpenseFilterRequest expenseFilterRequest, Pageable pageable){
        if (expenseFilterRequest == null) {
            return this.expenseTrackingService.getExpense(pageable);
        }
        return this.expenseTrackingService.getExpenseByDate(expenseFilterRequest.startDate(), expenseFilterRequest.endDate(), pageable);
    }

    @PostMapping("/expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid expense request body")
    })
    public void saveExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        this.expenseTrackingService.addExpense(expenseRequest);
    }

    @PostMapping("/expense/upload")
    public void uploadExpense(@RequestParam("file") MultipartFile file) {
        try {
            this.expenseTrackingService.saveFromFile(file);
        } catch (IOException e) {
            throw new CSVParsingException(e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense updated successfully"),
            @ApiResponse(responseCode = "400", description = "Expense id does not exist")
    })
    @PutMapping("/expense/{id}")
    public void updateExpense(@PathVariable Integer id, @Valid @RequestBody ExpenseRequest expenseRequest) {
        this.expenseTrackingService.updateExpense(id, expenseRequest);
    }

    @DeleteMapping("/expense/{id}")
    public void deleteExpense(@PathVariable Integer id) {
        this.expenseTrackingService.deleteExpense(id);
    }

    @GetMapping("/frequency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frequency found"),
    })
    public List<FrequencyResponse> getExpenseFrequency(){
        return this.frequencyService.getAllFrequencies();
    }

    @GetMapping("/category")
    public List<CategoryResponse> getCategories() {
        return this.categoryService.getAllCategories();
    }

    @PostMapping("/budget/status")
    public List<CurrentCategoryBudgetResponse> getBudgetStatus(@RequestBody ExpenseFilterRequest request) {
        return this.expenseTrackingService.getBudgetStatus(request);
    }
}
