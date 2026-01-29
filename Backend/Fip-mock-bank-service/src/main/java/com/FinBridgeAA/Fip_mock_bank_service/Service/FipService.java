package com.FinBridgeAA.Fip_mock_bank_service.Service;

import com.FinBridgeAA.Fip_mock_bank_service.DTO.BankAccountDTO;
import com.FinBridgeAA.Fip_mock_bank_service.DTO.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FipService {

    public List<BankAccountDTO> getFinancialData(UUID userId) {
        // MOCK DATA GENERATOR
        return List.of(
                new BankAccountDTO("HDFC", "ACC-HDFC-001", "SAVINGS", List.of(
                        new TransactionDTO("TXN-1", "2024-01-01", 50000, "CREDIT", "Salary"),
                        new TransactionDTO("TXN-2", "2024-01-05", 15000, "DEBIT", "Rent"))),
                new BankAccountDTO("SBI", "ACC-SBI-001", "SAVINGS", List.of(
                        new TransactionDTO("TXN-3", "2024-01-10", 3000, "DEBIT", "Shopping"))));
    }
}
