package com.FinBridgeAA.Account_aggregator_service.Client;

import com.FinBridgeAA.Account_aggregator_service.DTO.EncryptedPayload;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

@Component
public class FIUClient {

    private static KeyPair keyPair;

    static {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);
            keyPair = gen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey fetchPublicKey(String fiuId) {
        return keyPair.getPublic();
    }

    public void deliver(EncryptedPayload payload) {
        System.out.println("Encrypted payload delivered to FIU");
    }
}