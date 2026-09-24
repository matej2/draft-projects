package com.matej2.budget_lens.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ServiceConfig {
    @Bean("statsRestClient")
    public RestClient statsRestClient() {
        return RestClient.builder()
                .baseUrl("https://pxweb.stat.si:443/SiStatData/api/v1/sl/Data/H281S.px")
                .build();
    }
}
