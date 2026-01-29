package com.FinBridgeAA.user_service.Repository;

import com.FinBridgeAA.user_service.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, UUID> {
}