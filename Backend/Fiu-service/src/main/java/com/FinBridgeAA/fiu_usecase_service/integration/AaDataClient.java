package com.FinBridgeAA.fiu_usecase_service.integration;

import com.FinBridgeAA.fiu_usecase_service.common.DTO.BankAccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "account-aggregator-service", url = "http://localhost:8086")
public interface AaDataClient {

        @PostMapping("/aa/fetch-data")
        List<BankAccountDTO> fetchFinancialData(@RequestBody Map<String, String> request);
}