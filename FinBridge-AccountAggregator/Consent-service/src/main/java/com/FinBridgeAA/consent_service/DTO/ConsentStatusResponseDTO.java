package com.FinBridgeAA.consent_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConsentStatusResponseDTO {
    private String consentId;
    private String status;
}