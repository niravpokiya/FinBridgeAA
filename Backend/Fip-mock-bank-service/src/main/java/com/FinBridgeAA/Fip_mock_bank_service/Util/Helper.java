package com.FinBridgeAA.Fip_mock_bank_service.Util;

import com.FinBridgeAA.Fip_mock_bank_service.DTO.BankAccountDTO;
import com.FinBridgeAA.Fip_mock_bank_service.DTO.TransactionDTO;
import com.FinBridgeAA.Fip_mock_bank_service.Entity.BankAccountEntity;
import com.FinBridgeAA.Fip_mock_bank_service.Entity.TransactionEntity;
import jakarta.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static List<BankAccountDTO> ConvertToDto(List<BankAccountEntity> e) {
        List<BankAccountDTO> dtos = new ArrayList<>();
        for(BankAccountEntity b : e) {
            BankAccountDTO dto = new BankAccountDTO();
            dto.setAccountId(b.getAccountId());
            dto.setBankName(b.getBankName());
            dto.setAccountType(b.getAccountType());
            dto.setTransactions(convertToTransactionDTO(b.getTransactions()));
            dtos.add(dto);
        }
        return dtos;
    }
    public static List<TransactionDTO> convertToTransactionDTO(List<TransactionEntity> e) {
        List<TransactionDTO> dtos = new ArrayList<>();
        for(TransactionEntity t: e) {
            TransactionDTO dto = new TransactionDTO();
            dto.setTransactionId(t.getTransactionId());
            dto.setAmount(t.getAmount());
            dtos.add(dto);
        }
        return dtos;
    }
}
