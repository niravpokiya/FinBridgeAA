package com.FinBridgeAA.Fip_mock_bank_service.Repository;

import com.FinBridgeAA.Fip_mock_bank_service.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {

}
