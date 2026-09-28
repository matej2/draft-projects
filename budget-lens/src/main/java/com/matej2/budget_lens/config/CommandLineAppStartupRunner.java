package com.matej2.budget_lens.config;

import com.matej2.budget_lens.domain.entity.RecordLimit;
import com.matej2.budget_lens.repository.domain.ExpenseRepository;
import com.matej2.budget_lens.repository.domain.RecordLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final ExpenseRepository expenseRepository;
    private final RecordLimiter recordLimiter;


    @Override
    public void run(String...args) {
        long expenseCount = expenseRepository.count();
        RecordLimit recordLimit = recordLimiter.findOneByClassName("Expense");
        recordLimit.setCurrentCount(expenseCount);
        recordLimiter.save(recordLimit);
    }
}