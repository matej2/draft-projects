package com.matej2.budget_lens.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtils {

    public static Integer toPercentage(double number) {
        return MathUtils.toTwoDecimals(number * 100);
    }

    public static Integer toTwoDecimals(Double number) {
        if (number == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.HALF_UP);

        return Integer.valueOf(df.format(number));
    }
}
