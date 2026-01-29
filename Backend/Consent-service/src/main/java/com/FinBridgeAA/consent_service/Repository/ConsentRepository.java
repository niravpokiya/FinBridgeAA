package com.FinBridgeAA.consent_service.Repository;

import com.FinBridgeAA.consent_service.Entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, UUID> {
    Optional<Consent> findFirstByUserIdAndStatusOrderByUpdatedAtDesc(UUID userId, String status);
}