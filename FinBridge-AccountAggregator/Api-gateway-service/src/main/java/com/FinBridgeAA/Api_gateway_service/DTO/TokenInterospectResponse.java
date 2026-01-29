package com.FinBridgeAA.Api_gateway_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenInterospectResponse {
    private boolean active;
    private String userId;
    private String role;
    private String reason;

    public TokenInterospectResponse(boolean active, String userId, String role, String reason) {
        this.active = active;
        this.userId = userId;
        this.role = role;
        this.reason = reason;
    }
}
