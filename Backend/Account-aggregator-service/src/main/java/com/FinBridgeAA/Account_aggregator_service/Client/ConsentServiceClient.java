package com.FinBridgeAA.Account_aggregator_service.Client;

import com.FinBridgeAA.Account_aggregator_service.DTO.ConsentArtifact;
import org.springframework.stereotype.Component;

@Component
public class ConsentServiceClient {

    public ConsentArtifact fetchConsent(String consentId) {
        return new ConsentArtifact(
                consentId,
                true,
                "FIU01",
                "USER001");
    }
}