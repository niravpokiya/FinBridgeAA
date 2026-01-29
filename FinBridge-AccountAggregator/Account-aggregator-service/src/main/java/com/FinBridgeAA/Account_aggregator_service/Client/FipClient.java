package com.FinBridgeAA.Account_aggregator_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "fip-service", url = "http://localhost:8087")
public interface FipClient {

    @GetMapping("/bank/{userId}/financial-data")
    List<Map<String, Object>> fetchFinancialData(@PathVariable("userId") String userId);
}