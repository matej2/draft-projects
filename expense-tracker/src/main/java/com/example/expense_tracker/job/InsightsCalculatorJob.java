package com.example.expense_tracker.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InsightsCalculatorJob {

    @Scheduled(fixedDelayString = "PT1S")
    public void calculateInsights() {
        System.out.println("InsightsCalculatorJob started");
    }
}
