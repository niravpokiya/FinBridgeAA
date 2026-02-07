package com.FinBridgeAA.Fip_mock_bank_service.Service;

import com.FinBridgeAA.Fip_mock_bank_service.DTO.BankAccountDTO;
import com.FinBridgeAA.Fip_mock_bank_service.Repository.BankAccountRepo;
import com.FinBridgeAA.Fip_mock_bank_service.Repository.TransactionRepo;
import com.FinBridgeAA.Fip_mock_bank_service.Util.DummyDataGenerator;
import com.FinBridgeAA.Fip_mock_bank_service.Util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankDataService {
    @Autowired
    private BankAccountRepo bankAccountRepo;
    @Autowired
    private TransactionRepo transactionRepo;

    public List<BankAccountDTO> getFinancialData(String userId) {
        return Helper.ConvertToDto(bankAccountRepo.findByUser_UserId(userId));
    }

    public List<BankAccountDTO> getBankData(String userId, int months) {
        return DummyDataGenerator.generateMockData(userId, months);
    }
}