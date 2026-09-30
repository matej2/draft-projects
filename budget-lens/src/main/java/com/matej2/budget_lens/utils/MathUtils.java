package com.matej2.budget_lens.utils;

public class MathUtils {

    public static Float toTwoDecimals(Float number) {
        if (number == null) {
            return null;
        }
        return (float) (Math.round(number * 100.0) / 100.0);
    }
}
