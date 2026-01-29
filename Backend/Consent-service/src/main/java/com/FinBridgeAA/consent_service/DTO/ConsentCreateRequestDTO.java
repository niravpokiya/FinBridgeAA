package com.FinBridgeAA.consent_service.DTO;

import com.FinBridgeAA.consent_service.Enums.ConsentPurpose;
import com.FinBridgeAA.consent_service.Enums.FinancialDataType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Data
public class ConsentCreateRequestDTO {

    @NotNull
    @NotBlank(message = "userId is required")
    private String userId;

    @NotNull
    @NotBlank(message = "consent purpose is required")
    private ConsentPurpose purpose;

    @NotNull
    @NotBlank(message = "consent type is required")
    private FinancialDataType dataType;

    @NotNull
    @NotBlank(message = "fromDate is required")
    private LocalDate fromDate;

    @NotNull
    @NotBlank(message = "toDate is required")
    private LocalDate toDate;
}