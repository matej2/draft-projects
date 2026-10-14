package com.matej2.budget_lens.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static List<Calendar> generateCalendarInstancesForLastTreeMonths() {
        List<Calendar> result = new ArrayList<>();
        Calendar calendarMonth = Calendar.getInstance();

        java.util.stream.IntStream
                .rangeClosed(0, 6)
                .forEach(
                        i -> {
                            calendarMonth.add(Calendar.MONTH, -i);
                            result.add((Calendar)calendarMonth.clone());
                            calendarMonth.setTime(new Date());
                        }
                );

        return result;
    }
}
