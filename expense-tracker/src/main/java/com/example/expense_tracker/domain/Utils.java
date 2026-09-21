package com.example.expense_tracker.domain;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    public static float roundToTwoDecimals(double number) {
        return (float) (Math.round(number * 100.0) / 100.0);
    }

    public static CSVReader getCsvReader(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        return new CSVReader(reader);
    }
}
