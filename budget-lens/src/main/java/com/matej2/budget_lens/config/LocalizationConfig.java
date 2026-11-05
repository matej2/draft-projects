package com.matej2.budget_lens.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

@Configuration
public class LocalizationConfig {
    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("yyyy-MM")
                .toFormatter(Locale.ENGLISH);
    }
}
