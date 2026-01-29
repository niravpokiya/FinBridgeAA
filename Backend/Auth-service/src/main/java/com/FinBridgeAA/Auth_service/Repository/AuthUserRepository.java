package com.FinBridgeAA.Auth_service.Repository;

import com.FinBridgeAA.Auth_service.Entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findByPhoneNumber(String phoneNumber);
}
