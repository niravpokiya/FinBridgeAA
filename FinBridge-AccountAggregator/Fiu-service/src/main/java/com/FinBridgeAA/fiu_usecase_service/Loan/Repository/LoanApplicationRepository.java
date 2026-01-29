package com.FinBridgeAA.fiu_usecase_service.Loan.Repository;

import com.FinBridgeAA.fiu_usecase_service.Loan.Entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {
    List<LoanApplication> findByUserIdOrderByCreatedAtDesc(String userId);
}
