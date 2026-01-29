package com.FinBridgeAA.user_service.Controller;

import com.FinBridgeAA.user_service.DTO.KycStatusResponseDto;
import com.FinBridgeAA.user_service.DTO.UserProfileResponseDto;
import com.FinBridgeAA.user_service.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDto> getProfile(
            @RequestHeader("X-USER-ID") UUID userId) {

        return ResponseEntity.ok(service.getProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponseDto> updateProfile(
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestBody UserProfileResponseDto dto) {

        return ResponseEntity.ok(service.updateProfile(userId, dto));
    }

    @GetMapping("/kyc-status")
    public ResponseEntity<KycStatusResponseDto> getKycStatus(
            @RequestHeader("X-USER-ID") UUID userId) {

        return ResponseEntity.ok(
                new KycStatusResponseDto(userId, service.getKycStatus(userId))
        );
    }
}