package com.example.expense_tracker.domain.dto;

import java.time.LocalDate;

public record CSVImportRow(
        LocalDate date,
        String refNum,
        String subject,
        String description,
        Float positiveTraffic,
        Float negativeTraffic
) {
}
