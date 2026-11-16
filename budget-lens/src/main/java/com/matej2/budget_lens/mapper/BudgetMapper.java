package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.model.entity.Budget;
import com.matej2.budget_lens.model.request.BudgetRequest;
import com.matej2.budget_lens.model.response.BudgetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BudgetMapper {
    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "frequencyId", target = "frequency.id")
    Budget budgetRequestToBudget(BudgetRequest budgetRequest);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "frequency.id", target = "frequencyId")
    BudgetResponse budgetToBudgetResponse(Budget budget);
}
