package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.model.entity.Expense;
import com.matej2.budget_lens.model.integration.AiCategorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class AICategorizationMapper {
    public AiCategorizationRequest mapToRequest(Expense expense) {
        return new AiCategorizationRequest(
                expense.getId(),
                expense.getNote(),
                expense.getSource(),
                expense.getExpenseDate()
        ) ;
    }


}
