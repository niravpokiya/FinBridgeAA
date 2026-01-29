package com.FinBridgeAA.user_service.Entity;

import com.FinBridgeAA.user_service.Enums.KycStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @Column(name = "auth_user_id", nullable = false, updatable = false)
    private UUID userId;

    private String name;
    private String email;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}