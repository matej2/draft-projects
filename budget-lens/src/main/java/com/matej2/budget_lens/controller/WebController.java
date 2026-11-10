package com.matej2.budget_lens.controller;

import com.matej2.budget_lens.model.request.BudgetRequest;
import com.matej2.budget_lens.model.response.CategoryInsightsResponse;
import com.matej2.budget_lens.model.response.CategoryResponse;
import com.matej2.budget_lens.model.response.ExpenseResponse;
import com.matej2.budget_lens.model.response.FrequencyResponse;
import com.matej2.budget_lens.exception.CSVParsingException;
import com.matej2.budget_lens.exception.RecordOverLimitExeption;
import com.matej2.budget_lens.service.domain.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.matej2.budget_lens.utils.HttpUtils.getRedirectString;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ExpenseTrackingService expenseTrackingService;
    private final FileProcessorService fileProcessorService;
    private final FrequencyService frequencyService;
    private final CategoryService categoryService;
    private final InsightService insightService;
    private final BudgetService budgetService;

    private final WebModelProvider webModelProvider;
    private final DateTimeFormatter dateTimeFormatter;


    @GetMapping(WebModelProvider.DASHBOARD_PATH)
    public String home(Model model, Integer pageNumber) {
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 0;
        }
        Pageable sortedByDate = PageRequest.of(
                pageNumber,
                15,
                Sort.by("expenseDate").descending());
        List<ExpenseResponse> expenses = expenseTrackingService.getExpenseList(sortedByDate);

        Map<String, Object> attributes = webModelProvider.getAttributesForExpenses(pageNumber, expenses);
        model.addAllAttributes(attributes);

        return "dashboard";
    }

    @GetMapping(WebModelProvider.BUDGET_PATH)
    public String budget(Model model, @NonNull String yearMonth, @NonNull Integer categoryId) {
        YearMonth expenseYearMonth = YearMonth.parse(yearMonth, dateTimeFormatter);
        CategoryResponse categoryResponse = categoryService.getCategory(categoryId);
        List<CategoryInsightsResponse> insightsByDateAndCategory = insightService.getInsightByYearMonthAndCategory(expenseYearMonth, categoryResponse);

        Map<FrequencyResponse, BigDecimal> frequencyToBudget = new HashMap<>();
        for (CategoryInsightsResponse categoryInsight : insightsByDateAndCategory) {
            frequencyToBudget.putAll(frequencyService.generateFrequencyToBudgetMapping(categoryInsight));
        }

        model.addAllAttributes(webModelProvider.getAttributesForBudget(
            insightsByDateAndCategory,
            frequencyToBudget,
            yearMonth,
            categoryResponse
        ));

        return "budget";
    }

    @GetMapping(WebModelProvider.ABOUT_PATH)
    public String about() {
        return "index";
    }

    @PostMapping("/submitExpense")
    public String submitExpense(
            HttpServletRequest request,
            @ModelAttribute BudgetRequest budgetRequest,
            RedirectAttributes redirectAttributes
    ) {
        try {
            this.budgetService.save(budgetRequest);
        } catch (RecordOverLimitExeption e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return getRedirectString(request);
    }

    @DeleteMapping("/deleteExpense/{id}")
    public String deleteExpense(HttpServletRequest request, @PathVariable Integer id){
        this.expenseTrackingService.deleteExpense(id);
        return getRedirectString(request);
    }

    @PostMapping("/uploadCsv")
    public String uploadCsv(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        try {
            this.fileProcessorService.saveExpenseListFromFile(file);
        } catch (IOException e) {
            throw new CSVParsingException(e.getMessage());
        }
        return getRedirectString(request);
    }

}
