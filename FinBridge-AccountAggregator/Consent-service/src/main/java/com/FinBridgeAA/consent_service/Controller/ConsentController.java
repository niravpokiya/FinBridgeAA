package com.FinBridgeAA.consent_service.Controller;

import com.FinBridgeAA.consent_service.Entity.Consent;
import com.FinBridgeAA.consent_service.Service.ConsentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/consents")
public class ConsentController {

    private final ConsentService consentService;

    public ConsentController(ConsentService consentService) {
        this.consentService = consentService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getActiveConsent(@PathVariable UUID userId) {
        Optional<Consent> consent = consentService.getActiveConsent(userId);
        if (consent.isPresent()) {
            return ResponseEntity.ok(Map.of("status", "ACTIVE", "consentId", consent.get().getConsentId()));
        }
        return ResponseEntity.ok(Map.of("status", "NOT_FOUND"));
    }

    @PostMapping
    public ResponseEntity<Consent> createConsent(@RequestBody Map<String, String> request) {
        UUID userId = UUID.fromString(request.get("userId"));
        return ResponseEntity.ok(consentService.createConsent(userId));
    }

    @PostMapping("/{consentId}/approve")
    public ResponseEntity<String> approveConsent(@PathVariable UUID consentId) {
        consentService.approveConsent(consentId);
        return ResponseEntity.ok("Consent APPROVED");
    }

    @PostMapping("/{consentId}/decline")
    public ResponseEntity<String> declineConsent(@PathVariable UUID consentId) {
        consentService.declineConsent(consentId);
        return ResponseEntity.ok("Consent REJECTED");
    }
}