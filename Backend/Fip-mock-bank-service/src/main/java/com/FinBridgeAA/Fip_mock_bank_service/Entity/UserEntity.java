package com.FinBridgeAA.Fip_mock_bank_service.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    private String userId;

    @Column(nullable = false, unique = true)
    private String phone;

    private String name;
    private String pan;
}
