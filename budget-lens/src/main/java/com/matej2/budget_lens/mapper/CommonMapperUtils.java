package com.matej2.budget_lens.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    static Double toPercent(Double input) {
        if (input == null) {
            return null;
        }
        BigDecimal decimalInput = BigDecimal.valueOf(input * 100);
        return CommonMapperUtils.round(0, decimalInput);
    }

    @Named("toTwoDecimals")
    static Double toTwoDecimals(BigDecimal number) {
        return CommonMapperUtils.round(2, number);
    }

    private static Double round(Integer places, BigDecimal number) {
        BigDecimal rounded = number.setScale(places, RoundingMode.HALF_UP);
        return rounded.doubleValue();
    }

}
