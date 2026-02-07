package com.FinBridgeAA.Fip_mock_bank_service.Repository;

import com.FinBridgeAA.Fip_mock_bank_service.DTO.BankAccountDTO;
import com.FinBridgeAA.Fip_mock_bank_service.Entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepo extends JpaRepository<BankAccountEntity, Integer> {
    List<BankAccountEntity> findByUser_UserId(String userId);
}
