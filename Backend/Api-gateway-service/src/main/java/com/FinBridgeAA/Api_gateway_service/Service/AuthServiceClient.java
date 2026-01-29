package com.FinBridgeAA.Api_gateway_service.Service;

import com.FinBridgeAA.Api_gateway_service.DTO.TokenInterospectResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceClient {

    private final WebClient webClient;

    // In a real scenario, use service discovery (e.g. lb://Auth-service) if Eureka
    // is present.
    // For now, using direct URL or properties.
    public AuthServiceClient(WebClient.Builder webClientBuilder,
            @Value("${auth.service.url:http://localhost:8081}") String authUrl) {
        this.webClient = webClientBuilder.baseUrl(authUrl).build();
    }

    public Mono<TokenInterospectResponse> validateToken(String token) {
        // Ensure token has Bearer prefix if not present, though likely passed raw
        // The Controller expects "Authorization" header with "Bearer <token>"
        String headerValue = token.startsWith("Bearer ") ? token : "Bearer " + token;

        return webClient.post()
                .uri("/auth/token/interospect")
                .header("Authorization", headerValue)
                .retrieve()
                .bodyToMono(TokenInterospectResponse.class)
                .onErrorResume(e -> {
                    // Fallback in case of error (e.g. Auth service down)
                    return Mono.just(new TokenInterospectResponse(false, null, null, "AUTH_SERVICE_UNAVAILABLE"));
                });
    }
}
