package com.FinBridgeAA.consent_service.Service;

import com.FinBridgeAA.consent_service.Entity.Consent;
import com.FinBridgeAA.consent_service.Repository.ConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsentService {

    private final ConsentRepository consentRepository;

    @Transactional(readOnly = true)
    public Optional<Consent> getActiveConsent(UUID userId) {
        return consentRepository.findFirstByUserIdAndStatusOrderByUpdatedAtDesc(userId, "ACTIVE");
    }

    public Consent createConsent(UUID userId) {
        Consent consent = new Consent();
        consent.setUserId(userId);
        consent.setStatus("ACTIVE");
        consent.setCreatedAt(LocalDateTime.now());
        consent.setUpdatedAt(LocalDateTime.now());
        return consentRepository.save(consent);
    }

    public void approveConsent(UUID consentId) {
        Consent consent = consentRepository.findById(consentId)
                .orElseThrow(() -> new RuntimeException("Consent not found"));
        consent.setStatus("ACTIVE");
        consent.setUpdatedAt(LocalDateTime.now());
        consentRepository.save(consent);
    }

    public void declineConsent(UUID consentId) {
        Consent consent = consentRepository.findById(consentId)
                .orElseThrow(() -> new RuntimeException("Consent not found"));
        consent.setStatus("REJECTED");
        consent.setUpdatedAt(LocalDateTime.now());
        consentRepository.save(consent);
    }
}