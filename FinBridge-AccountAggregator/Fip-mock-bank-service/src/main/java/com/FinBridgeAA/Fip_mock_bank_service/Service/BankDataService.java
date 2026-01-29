package com.FinBridgeAA.Fip_mock_bank_service.Service;

import com.FinBridgeAA.Fip_mock_bank_service.DTO.BankAccountDTO;
import com.FinBridgeAA.Fip_mock_bank_service.Util.DummyDataGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankDataService {

    public List<BankAccountDTO> getFinancialData(String userId) {
        // In a real scenario, fetch from DB. Here we mock it.
        return DummyDataGenerator.generateMockData(userId);
    }

    public List<BankAccountDTO> getBankData(String userId, int months) {
        return DummyDataGenerator.generateMockData(userId, months);
    }
}