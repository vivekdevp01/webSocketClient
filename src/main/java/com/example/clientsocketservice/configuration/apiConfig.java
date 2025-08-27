package com.example.clientsocketservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class apiConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
