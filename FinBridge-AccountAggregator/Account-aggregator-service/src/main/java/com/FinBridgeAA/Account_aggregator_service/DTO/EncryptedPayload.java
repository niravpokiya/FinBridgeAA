package com.FinBridgeAA.Account_aggregator_service.DTO;

public class EncryptedPayload {

    private String encryptedData;
    private String encryptedKey;

    public EncryptedPayload(String encryptedData, String encryptedKey) {
        this.encryptedData = encryptedData;
        this.encryptedKey = encryptedKey;
    }

    public String getEncryptedData() { return encryptedData; }
    public String getEncryptedKey() { return encryptedKey; }
}
