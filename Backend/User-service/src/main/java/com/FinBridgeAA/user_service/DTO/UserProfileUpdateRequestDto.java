package com.FinBridgeAA.user_service.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileUpdateRequestDto {
    private String name;
    private String email;
    private LocalDate dob;
}