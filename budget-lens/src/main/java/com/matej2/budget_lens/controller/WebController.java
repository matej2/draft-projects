package com.matej2.budget_lens.controller;

import com.matej2.budget_lens.domain.dto.request.BudgetRequest;
import com.matej2.budget_lens.domain.dto.response.CategoryInsightsResponse;
import com.matej2.budget_lens.domain.dto.response.CategoryResponse;
import com.matej2.budget_lens.domain.dto.response.ExpenseResponse;
import com.matej2.budget_lens.domain.dto.response.FrequencyResponse;
import com.matej2.budget_lens.domain.mapper.CategoryMapper;
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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.matej2.budget_lens.utils.ControllerUtils.getRedirectString;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ExpenseTrackingService expenseTrackingService;
    private final FrequencyService frequencyService;
    private final CategoryService categoryService;
    private final InsightService insightService;
    private final BudgetService budgetService;

    private final WebModelProvider webModelProvider;
    private final DateTimeFormatter dateTimeFormatter;

    private final CategoryMapper categoryMapper;


    @GetMapping
    public String home(Model model, Integer pageNumber) {
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 0;
        }
        Pageable sortedByDate = PageRequest.of(
                pageNumber,
                15,
                Sort.by("expenseDate").descending());
        List<ExpenseResponse> expenses = expenseTrackingService.getExpense(sortedByDate);

        Map<String, Object> attributes = webModelProvider.getAttributesForExpenses(pageNumber, expenses);
        model.addAllAttributes(attributes);

        return "index";
    }

    private Map<FrequencyResponse, Integer> generateFrequencyToBudgetMapping(CategoryInsightsResponse insight) {
        List<FrequencyResponse> allFrequencies = frequencyService.getAllFrequencies();
        Map<FrequencyResponse, Integer> frequencyToBudget = new HashMap<>();

        FrequencyResponse monthlyFrequency = allFrequencies.stream()
                .filter(f -> f.number() == 12)
                .findFirst()
                .orElseThrow();

        double budgetYearlyValue = insight.percentile90() * monthlyFrequency.number();

        for (FrequencyResponse frequency : allFrequencies) {
            long relativeBudget = Math.round(budgetYearlyValue / frequency.number());
            frequencyToBudget.put(frequency, Math.toIntExact(relativeBudget));
        }

        return frequencyToBudget;
    }

    @GetMapping("/budget")
    public String budget(Model model, @NonNull String yearMonth, @NonNull Integer categoryId) {
        YearMonth expenseYearMonth = YearMonth.parse(yearMonth, dateTimeFormatter);
        CategoryResponse categoryResponse = categoryService.getCategory(categoryId);
        List<CategoryInsightsResponse> insightsByDateAndCategory = insightService.getInsightByYearMonthAndCategory(expenseYearMonth, categoryResponse);

        Map<FrequencyResponse, Integer> frequencyToBudget = new HashMap<>();
        for (CategoryInsightsResponse categoryInsight : insightsByDateAndCategory) {
            frequencyToBudget.putAll(generateFrequencyToBudgetMapping(categoryInsight));
        }

        model.addAllAttributes(webModelProvider.getAttributesForBudget(
            insightsByDateAndCategory,
            frequencyToBudget,
            yearMonth,
            categoryResponse
        ));

        return "budget";
    }



    @PostMapping("/submitExpense")
    public String submitExpense(
            HttpServletRequest request,
            @ModelAttribute BudgetRequest budgetRequest,
            RedirectAttributes redirectAttributes
    ) {
        try {
            this.budgetService.add(budgetRequest);
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
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            this.expenseTrackingService.saveFromFile(file);
        } catch (IOException e) {
            throw new CSVParsingException(e.getMessage());
        }
        return "redirect:/";
    }

}
