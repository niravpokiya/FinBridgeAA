package com.FinBridgeAA.Account_aggregator_service.Policy;

import com.FinBridgeAA.Account_aggregator_service.DTO.ConsentArtifact;
import com.FinBridgeAA.Account_aggregator_service.DTO.DataRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class AAPolicyEnforcer {

    public void enforce(ConsentArtifact consent, DataRequestDTO request) {

        if (!consent.isActive()) {
            throw new IllegalStateException("Consent is not active");
        }

        if (!consent.getAllowedFiuId().equals(request.getFiuId())) {
            throw new IllegalStateException("FIU not allowed by consent");
        }
    }
}