package com.FinBridgeAA.Auth_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserClient {

    @PostMapping("/internal/user/create")
    ResponseEntity<String> createUser(@RequestHeader("X-USER-ID") UUID userId);
}
