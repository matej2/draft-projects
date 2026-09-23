package com.example.expense_tracker.utils;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class CsvUtils {
    public static float roundToTwoDecimals(double number) {
        return (float) (Math.round(number * 100.0) / 100.0);
    }

    public static CSVReader getCsvReader(InputStream input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        return new CSVReader(reader);
    }

    public static List<String> generateMonths() {
        YearMonth now = YearMonth.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        return java.util.stream.IntStream.rangeClosed(1, currentMonth-1)
                .mapToObj(month -> String.format("%04dM%02d", currentYear, month))
                .collect(Collectors.toList());
    }
}
