package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.utils.MathUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.math.BigDecimal;
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
        return MathUtils.toPercentage(input);
    }

}
