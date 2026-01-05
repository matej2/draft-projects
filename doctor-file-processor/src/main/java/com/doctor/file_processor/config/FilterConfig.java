package com.doctor.file_processor.config;


import com.doctor.file_processor.middleware.RateLimitFilter;
import com.doctor.file_processor.service.AccessInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Value("${rateLimit}")
    private Integer rateLimit;

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter(AccessInfoService accessInfoService) {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter(accessInfoService, rateLimit));
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}