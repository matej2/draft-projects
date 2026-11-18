package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.model.entity.Expense;
import com.matej2.budget_lens.model.integration.AiCategorizationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AICategorizationMapper {

    AICategorizationMapper INSTANCE = Mappers.getMapper(AICategorizationMapper.class);

    @Mapping(source = "id", target = "expenseId")
    @Mapping(source = "note", target = "description")
    @Mapping(source = "source.source", target = "merchant")
    @Mapping(source = "expenseDate", target = "date")
    AiCategorizationRequest expenseToAiCategorizationRequest(Expense expense);
}
