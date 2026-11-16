package com.matej2.budget_lens.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Mapper
public interface CommonMapperUtils {
    @Named("toBigDecimal")
    static BigDecimal toBigDecimal(Float input) {
        return new BigDecimal(input);
    }

    @Named("toLocalDate")
    static LocalDate toLocalDate(String input) {
        return LocalDate.parse(input);
    }

    @Named("toPercent")
    static Integer toPercent(Double input) {
        if (input == null) {
            return null;
        }
        return CommonMapperUtils.toTwoDecimals(input * 100);
    }

    @Named("toTwoDecimals")
    static Integer toTwoDecimals(double number) {
        DecimalFormat df = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.HALF_UP);

        return Integer.valueOf(df.format(number));
    }

}
