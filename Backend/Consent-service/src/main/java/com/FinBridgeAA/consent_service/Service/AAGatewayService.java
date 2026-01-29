package com.FinBridgeAA.consent_service.Service;

import com.FinBridgeAA.consent_service.Entity.Consent;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AAGatewayService {

    public String createConsentInAA(Consent consent) {

        // This is a dummy for now Later this will be a REST call to AA
        return "AA-CONSENT-" + UUID.randomUUID();
    }
}
