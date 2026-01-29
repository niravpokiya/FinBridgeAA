package com.FinBridgeAA.Account_aggregator_service.Audit;

import com.FinBridgeAA.Account_aggregator_service.DTO.ConsentArtifact;
import com.FinBridgeAA.Account_aggregator_service.DTO.DataRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {

    public void logSuccess(DataRequestDTO request, ConsentArtifact consent) {
        System.out.println(
                "AUDIT | consentId=" + consent.getConsentId()
                        + " | fiu=" + request.getFiuId()
                        + " | status=DELIVERED"
        );
    }
}
