package com.FinBridgeAA.Fip_mock_bank_service.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
public class TransactionEntity {
    @Id
    private String transactionId;
    private LocalDate date;
    private double amount;
    private String type;
    private String narration;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccountEntity account;
}
