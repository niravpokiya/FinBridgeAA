package com.FinBridgeAA.Fip_mock_bank_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String transactionId;
    private String date;
    private double amount;
    private String type;
    private String narration;
}
