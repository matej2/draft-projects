package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.model.csv.CSVImportRow;
import com.matej2.budget_lens.model.entity.Expense;
import com.matej2.budget_lens.model.request.ExpenseRequest;
import com.matej2.budget_lens.model.response.ExpenseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CommonMapperUtils.class)
public interface ExpenseMapper {
    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    @Mapping(source = "categoryId", target = "category.id")
    Expense expenseRequestToExpense(ExpenseRequest expenseRequest);

    @Mapping(source = "category", target = "category.id")
    Expense expenseResponseToExpense(ExpenseResponse expenseResponse);

    @Mapping(source = "negativeTraffic", target = "cost", qualifiedByName = "toBigDecimal")
    @Mapping(source = "date", target = "expenseDate", qualifiedByName = "toLocalDate")
    Expense csvImportRowToExpense(CSVImportRow csvImportRow);

    @Mapping(source = "category.id", target = "category")
    ExpenseResponse expenseToExpenseResponse(Expense expense);

}
