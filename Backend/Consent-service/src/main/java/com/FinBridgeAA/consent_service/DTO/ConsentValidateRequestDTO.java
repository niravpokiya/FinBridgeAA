package com.FinBridgeAA.consent_service.DTO;

import com.FinBridgeAA.consent_service.Enums.ConsentPurpose;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class ConsentValidateRequestDTO {
    @NonNull
    @NotBlank(message = "consentId is required")
    private String consentId;

    @NonNull
    @NotBlank(message = "purpose is required")
    private ConsentPurpose purpose;
}