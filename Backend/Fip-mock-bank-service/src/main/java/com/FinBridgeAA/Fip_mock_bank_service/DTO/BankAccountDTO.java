package com.FinBridgeAA.Fip_mock_bank_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
    private String bankName;
    private String accountId;
    private String accountType;
    private List<TransactionDTO> transactions;
}
