package com.FinBridgeAA.Account_aggregator_service.Crypto;

import com.FinBridgeAA.Account_aggregator_service.DTO.EncryptedPayload;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.security.PublicKey;
import java.util.Base64;

@Service
public class EncryptionService {

    private final KeyResolver keyResolver;

    public EncryptionService(KeyResolver keyResolver) {
        this.keyResolver = keyResolver;
    }

    public EncryptedPayload encrypt(byte[] plainData, String fiuId) {
        try {
            PublicKey fiuPublicKey = keyResolver.resolvePublicKey(fiuId);

            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey aesKey = keyGen.generateKey();

            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encryptedData = aesCipher.doFinal(plainData);

            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, fiuPublicKey);
            byte[] encryptedKey = rsaCipher.doFinal(aesKey.getEncoded());

            return new EncryptedPayload(
                    Base64.getEncoder().encodeToString(encryptedData),
                    Base64.getEncoder().encodeToString(encryptedKey)
            );

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
}
