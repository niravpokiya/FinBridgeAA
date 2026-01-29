package com.FinBridgeAA.fiu_usecase_service.Loan.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID applicationId;

    private String userId;
    private int requestedAmount;
    private int tenureMonths;

    private String status; // APPROVED, REJECTED, CONSENT_REQUIRED
    private String remarks; // e.g. "Max Loan: 50000"
    private int approvedAmount;

    private LocalDateTime createdAt;
}
