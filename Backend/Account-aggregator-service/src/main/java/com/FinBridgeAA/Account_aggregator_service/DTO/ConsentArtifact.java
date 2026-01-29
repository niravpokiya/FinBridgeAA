package com.FinBridgeAA.Account_aggregator_service.DTO;

public class ConsentArtifact {

    private String consentId;
    private boolean active;
    private String allowedFiuId;
    private String customerId;

    public ConsentArtifact(String consentId, boolean active, String allowedFiuId, String customerId) {
        this.consentId = consentId;
        this.active = active;
        this.allowedFiuId = allowedFiuId;
        this.customerId = customerId;
    }

    public String getConsentId() {
        return consentId;
    }

    public boolean isActive() {
        return active;
    }

    public String getAllowedFiuId() {
        return allowedFiuId;
    }

    public String getCustomerId() {
        return customerId;
    }
}