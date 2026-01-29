package com.FinBridgeAA.fiu_usecase_service.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "consent-service", url = "http://localhost:8083")
public interface CmsClient {

    @GetMapping("/consents/{userId}")
    Map<String, Object> getActiveConsent(@PathVariable("userId") UUID userId);

    @PostMapping("/consents")
    Map<String, Object> createConsent(@RequestBody Map<String, String> request);
}
