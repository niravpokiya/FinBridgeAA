package com.FinBridgeAA.Api_gateway_service;

import com.FinBridgeAA.Api_gateway_service.DTO.TokenInterospectResponse;
import com.FinBridgeAA.Api_gateway_service.Service.AuthServiceClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

class AuthConnectionTest {

    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void testConnectToAuthService_Success() {
        AuthServiceClient authServiceClient = new AuthServiceClient(
                WebClient.builder(),
                mockBackEnd.url("/").toString());

        MockResponse mockResponse = new MockResponse()
                .setBody("{\"active\": true, \"userId\": \"12345\", \"role\": \"USER\", \"reason\": null}")
                .addHeader("Content-Type", "application/json");

        mockBackEnd.enqueue(mockResponse);

        StepVerifier.create(authServiceClient.validateToken("test-token"))
                .expectNextMatches(response -> response.isActive() && response.getUserId().equals("12345"))
                .verifyComplete();
    }

    @Test
    void testConnectToAuthService_Failure() {
        AuthServiceClient authServiceClient = new AuthServiceClient(
                WebClient.builder(),
                mockBackEnd.url("/").toString());

        MockResponse mockResponse = new MockResponse()
                .setBody("{\"active\": false, \"userId\": null, \"role\": null, \"reason\": \"TOKEN_BLACKLISTED\"}")
                .addHeader("Content-Type", "application/json");

        mockBackEnd.enqueue(mockResponse);

        StepVerifier.create(authServiceClient.validateToken("bad-token"))
                .expectNextMatches(response -> !response.isActive() && "TOKEN_BLACKLISTED".equals(response.getReason()))
                .verifyComplete();
    }
}
