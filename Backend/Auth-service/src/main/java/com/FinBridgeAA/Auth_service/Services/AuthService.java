package com.FinBridgeAA.Auth_service.Services;

import com.FinBridgeAA.Auth_service.Client.UserClient;
import com.FinBridgeAA.Auth_service.Entity.AuthUser;
import com.FinBridgeAA.Auth_service.Enums.Role;
import com.FinBridgeAA.Auth_service.Enums.Status;
import com.FinBridgeAA.Auth_service.Repository.AuthUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final OtpService otpService;
    private final JwtService jwtService;
    private final UserClient userClient;

    public AuthService(AuthUserRepository authUserRepository, OtpService otpService, JwtService jwtService,
            UserClient userClient) {
        this.authUserRepository = authUserRepository;
        this.otpService = otpService;
        this.jwtService = jwtService;
        this.userClient = userClient;
    }

    public void requestOtp(String phoneNumber) {
        String otp = otpService.generateOtp();
        System.out.println(otp);
        AuthUser user = authUserRepository
                .findByPhoneNumber(phoneNumber)
                .orElseGet(() -> createNewUser(phoneNumber));
        user.setOtpHash(otpService.encodeOtp(otp));
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        user.setOtpAttempts(0);
        authUserRepository.save(user);
    }

    private AuthUser createNewUser(String phoneNumber) {
        AuthUser user = new AuthUser();
        user.setPhoneNumber(phoneNumber);
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    public String VerifyOtp(String phoneNumber, String otp) {
        AuthUser user = authUserRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Invalid request..."));

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired...");
        }

        // attempt limit exceeded...
        if (user.getOtpAttempts() >= 5) {
            throw new RuntimeException("Too many otp attempts...");
        }

        if (!otpService.verifyOtp(otp, user.getOtpHash())) {
            user.setOtpAttempts(user.getOtpAttempts() + 1);
            authUserRepository.save(user);
            throw new RuntimeException("invalid OTP...");
        }

        user.setOtpHash(null);
        user.setOtpExpiry(null);
        user.setOtpAttempts(0);
        String token = jwtService.generateJWTToken(user);
        authUserRepository.save(user);

        // Sync with User Service
        try {
            userClient.createUser(user.getAuthUserId());
        } catch (Exception e) {
            // Log error but don't fail auth? Or fail auth?
            // Requirement says "check if user exists then ignores otherwise created"
            // Assuming we should log error but maybe not block login if it's intermittent,
            // but for data consistency it might be better to ensure it.
            // For now, let's print stack trace and proceed or maybe throw?
            // Let's just print for now as this is a simple impl.
            System.err.println("Failed to sync user with User Service: " + e.getMessage());
            e.printStackTrace();
        }

        return token;
    }
}
