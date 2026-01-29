package com.FinBridgeAA.consent_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConsentValidateResponseDTO {
    private boolean valid;
    private String message;
}