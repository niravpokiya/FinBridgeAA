package com.FinBridgeAA.Api_gateway_service.Config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

        @Bean
        public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

                return builder.routes()

                                // Auth Service
                                .route("auth-service", r -> r
                                                .path("/api/auth/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("http://localhost:8081"))

                                // User Service
                                .route("user-service", r -> r
                                                .path("/api/users/**")
                                                .filters(f -> f.rewritePath("/api/users/(?<segment>.*)",
                                                                "/user/${segment}"))
                                                .uri("http://localhost:8082"))

                                // FIU Service
                                .route("fiu-service", r -> r
                                                .path("/api/fiu/**")
                                                .filters(f -> f.stripPrefix(2))
                                                .uri("http://localhost:8084"))

                                // Consent Service
                                .route("consent-service", r -> r
                                                .path("/api/consent/**")
                                                .filters(f -> f.stripPrefix(2))
                                                .uri("http://localhost:8085"))

                                // Account Aggregator
                                .route("aa-service", r -> r
                                                .path("/api/aa/**")
                                                .filters(f -> f.stripPrefix(2))
                                                .uri("http://localhost:8084"))

                                // FIP Mock Bank
                                .route("fip-service", r -> r
                                                .path("/api/fip/**")
                                                .filters(f -> f.stripPrefix(2))
                                                .uri("http://localhost:8086"))

                                .build();
        }
}