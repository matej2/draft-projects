package com.matej2.budget_lens.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matej2.budget_lens.repository.UserRepository;
import com.matej2.budget_lens.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

@Configuration
public class ApplicationConfig {
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository  userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("yyyy-MM")
                .toFormatter(Locale.ENGLISH);
    }

    @Bean
    @Qualifier("openaiRestTemplate")
    public RestClient openaiRestTemplate(RestClient.Builder builder,
                                         @Value("${openai.api.key}") String openaiApiKey) {
        return builder
                .baseUrl("https://api.openai.com/v1/")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                .build();
    }

}
