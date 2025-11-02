package com.project.aau.sw3.p3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpConfig {

    // Creates a RestTemplate bean to be used throughout the application
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
