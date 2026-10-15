package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.entity.Expense;
import com.matej2.budget_lens.domain.integration.AiCategorizationRequest;
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
