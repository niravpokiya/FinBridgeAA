package com.FinBridgeAA.Auth_service.Entity;

import com.FinBridgeAA.Auth_service.Enums.Role;
import com.FinBridgeAA.Auth_service.Enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, name = "auth_user_id")
    private UUID authUserId;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "otp_hash")
    private String otpHash;
    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;
    @Column(name = "otp_attempts")
    private int otpAttempts;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AuthUser() {
    }

    public AuthUser(UUID authUserId, String phoneNumber, String otpHash, LocalDateTime otpExpiry, int otpAttempts,
            Role role, Status status, LocalDateTime createdAt) {
        this.authUserId = authUserId;
        this.phoneNumber = phoneNumber;
        this.otpHash = otpHash;
        this.otpExpiry = otpExpiry;
        this.otpAttempts = otpAttempts;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(UUID authUserId) {
        this.authUserId = authUserId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public int getOtpAttempts() {
        return otpAttempts;
    }

    public void setOtpAttempts(int otpAttempts) {
        this.otpAttempts = otpAttempts;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
