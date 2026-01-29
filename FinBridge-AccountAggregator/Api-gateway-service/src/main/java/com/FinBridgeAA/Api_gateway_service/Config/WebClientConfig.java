package com.FinBridgeAA.Api_gateway_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        // Returns a builder that can be used to create WebClients
        return WebClient.builder();
    }
}
