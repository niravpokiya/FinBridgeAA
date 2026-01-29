package com.FinBridgeAA.Account_aggregator_service.DTO;

public class DataRequestDTO {

    private String userId;
    private String consentId;
    private String fiuId;

    public String getUserId() { return userId; }
    public String getConsentId() { return consentId; }
    public String getFiuId() { return fiuId; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setConsentId(String consentId) { this.consentId = consentId; }
    public void setFiuId(String fiuId) { this.fiuId = fiuId; }
}