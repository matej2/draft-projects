package com.matej2.budget_lens.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return LocalDate.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDate getFirstDayOfTheMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDateOfPreviousMonth = calendar.getTime();

        return convertToLocalDate(firstDateOfPreviousMonth);
    }

    public static LocalDate getLastDayOfTheMonth(Calendar calendar) {
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDateOfPreviousMonth = calendar.getTime();

        return convertToLocalDate(lastDateOfPreviousMonth);
    }
}
