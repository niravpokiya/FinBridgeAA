package com.FinBridgeAA.Api_gateway_service.Config;

import com.FinBridgeAA.Api_gateway_service.Service.JwtService;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private boolean isAuthorized(String path, String role) {

        if (path.startsWith("/api/fiu")) {
            return role.equals("FIU") || role.equals("USER");
        }

        if (path.startsWith("/api/consent")) {
            return role.equals("USER") || role.equals("FIU");
        }

        if (path.startsWith("/api/aa")) {
            return role.equals("FIU");
        }

        if (path.startsWith("/api/fip")) {
            return role.equals("AA");
        }

        return true;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("DEBUG: JwtFilter intercepting path: " + path);

        // Allow OPTIONS (CORS preflight)
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            System.out.println("DEBUG: Passing OPTIONS request");
            return chain.filter(exchange);
        }

        if (path.startsWith("/api/auth")) {
            System.out.println("DEBUG: Allowing /api/auth path");
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("DEBUG: No Bearer token found for protected path");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String token = authHeader.substring(7);
        if (!isAuthorized(path, jwtService.extractRole(token))) {
            System.out.println(
                    "DEBUG: Unauthorized access attempt. Path: " + path + ", Role: " + jwtService.extractRole(token));
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
        if (!jwtService.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String userId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);
        exchange = exchange.mutate()
                .request(r -> r
                        .header("X-User-Id", userId)
                        .header("X-User-Role", role))
                .build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Run early, but after Netty/CORS
    }
}