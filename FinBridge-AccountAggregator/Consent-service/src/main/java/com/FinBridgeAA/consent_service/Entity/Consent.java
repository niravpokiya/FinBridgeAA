package com.FinBridgeAA.consent_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID consentId;

    private UUID userId;

    @Column(nullable = false)
    private String status; // CREATED, APPROVED, ACTIVE, REVOKED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID getConsentId() {
        return consentId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}