package com.FinBridgeAA.user_service.DTO;

import com.FinBridgeAA.user_service.Enums.KycStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class KycStatusResponseDto {

    private UUID userId;
    private KycStatus status;

    public KycStatusResponseDto(UUID userId, KycStatus status) {
        this.userId = userId;
        this.status = status;
    }

    public UUID getUserId() {
        return userId;
    }

    public KycStatus getStatus() {
        return status;
    }
}