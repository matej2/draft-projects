package com.matej2.budget_lens.utils;

public class MathUtils {

    public static Double toPercentage(double number) {
        return MathUtils.toTwoDecimals(number * 100);
    }

    public static Double toTwoDecimals(Double number) {
        if (number == null) {
            return null;
        }
        return Math.round(number * 100.0) / 100.0;
    }
}
