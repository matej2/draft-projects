package com.example.expense_tracker.domain;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    public static float roundToTwoDecimals(double number) {
        return (float) (Math.round(number * 100.0) / 100.0);
    }

    public static CSVParser getCSVParser(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        CSVFormat format = CSVFormat.Builder.create().setIgnoreHeaderCase(true).setTrim(true).get();

        return CSVParser.builder().setReader(reader).setFormat(format).get();
    }
}
