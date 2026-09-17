package com.example.expense_tracker.domain;

public class Utils {
    public static float roundToTwoDecimals(double number) {
        return (float) (Math.round(number * 100.0) / 100.0);
    }
}
