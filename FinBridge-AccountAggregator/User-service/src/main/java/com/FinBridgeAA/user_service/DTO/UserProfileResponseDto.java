package com.FinBridgeAA.user_service.DTO;

import com.FinBridgeAA.user_service.Enums.KycStatus;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserProfileResponseDto {

    private UUID userId;
    private String name;
    private String email;
    private LocalDate dob;
    private KycStatus kycStatus;
    private Instant createdAt;

}