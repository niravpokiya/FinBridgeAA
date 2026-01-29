package com.FinBridgeAA.Account_aggregator_service.Orchestration;

import com.FinBridgeAA.Account_aggregator_service.Client.CmsClient;
import com.FinBridgeAA.Account_aggregator_service.Client.FipClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AaService {

    private final CmsClient cmsClient;
    private final FipClient fipClient;

    public List<Map<String, Object>> fetchData(UUID userId) {
        // 1. Verify Consent
        Map<String, Object> consent = cmsClient.getActiveConsent(userId);
        Object status = consent.get("status");

        if (!"ACTIVE".equals(status)) {
            throw new RuntimeException("No Active Consent found for User: " + userId);
        }

        // 2. Fetch Data from FIP
        // 2. Fetch Data from FIP
        return fipClient.fetchFinancialData(userId.toString());
    }
}
