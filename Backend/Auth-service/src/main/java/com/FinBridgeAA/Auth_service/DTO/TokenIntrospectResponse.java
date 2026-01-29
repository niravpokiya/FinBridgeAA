package com.FinBridgeAA.Auth_service.DTO;

public class TokenIntrospectResponse {
    private boolean active;
    private String userId;
    private String role;
    private String reason;

    public TokenIntrospectResponse() {
    }

    public TokenIntrospectResponse(boolean active, String userId, String role, String reason) {
        this.active = active;
        this.userId = userId;
        this.role = role;
        this.reason = reason;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
