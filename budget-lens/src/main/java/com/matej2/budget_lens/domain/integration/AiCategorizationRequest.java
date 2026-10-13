package com.matej2.budget_lens.domain.integration;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class AiCategorizationRequest {
        public Integer expenseId;
        public String description;
        public String merchant;
        public LocalDate date;
}
