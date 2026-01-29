package com.FinBridgeAA.Account_aggregator_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "consent-service", url = "http://localhost:8083")
public interface CmsClient {

    @GetMapping("/consents/{userId}")
    Map<String, Object> getActiveConsent(@PathVariable("userId") UUID userId);
}
