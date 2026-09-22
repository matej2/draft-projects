package com.example.expense_tracker.domain;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static float roundToTwoDecimals(double number) {
        return (float) (Math.round(number * 100.0) / 100.0);
    }

    public static CSVReader getCsvReader(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        return new CSVReader(reader);
    }

    public static List<String> generateMonths() {
        int currentYear = YearMonth.now().getYear();
        return java.util.stream.IntStream.rangeClosed(1, 12)
                .mapToObj(month -> String.format("%04dM%02d", currentYear, month))
                .collect(Collectors.toList());
    }
}
