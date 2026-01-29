package com.FinBridgeAA.Account_aggregator_service.Validation;

import com.FinBridgeAA.Account_aggregator_service.Client.ConsentServiceClient;
import com.FinBridgeAA.Account_aggregator_service.DTO.ConsentArtifact;
import org.springframework.stereotype.Component;

@Component
public class ConsentValidator {

    private final ConsentServiceClient consentClient;

    public ConsentValidator(ConsentServiceClient consentClient) {
        this.consentClient = consentClient;
    }

    public ConsentArtifact validate(String consentId) {
        return consentClient.fetchConsent(consentId);
    }
}
