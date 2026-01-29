package com.FinBridgeAA.Account_aggregator_service.Crypto;

import com.FinBridgeAA.Account_aggregator_service.Client.FIUClient;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
public class KeyResolver {

    private final FIUClient fiuClient;

    public KeyResolver(FIUClient fiuClient) {
        this.fiuClient = fiuClient;
    }

    public PublicKey resolvePublicKey(String fiuId) {
        return fiuClient.fetchPublicKey(fiuId);
    }
}