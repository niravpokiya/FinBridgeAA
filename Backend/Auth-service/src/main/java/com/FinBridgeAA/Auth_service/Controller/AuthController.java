package com.FinBridgeAA.Auth_service.Controller;

import com.FinBridgeAA.Auth_service.DTO.OtpRequest;
import com.FinBridgeAA.Auth_service.DTO.VerifyOtpRequest;
import com.FinBridgeAA.Auth_service.Services.AuthService;
import com.FinBridgeAA.Auth_service.Services.JwtService;
import com.FinBridgeAA.Auth_service.Services.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @PostMapping("/request-otp")
    public ResponseEntity<String> requestOTP(@RequestBody OtpRequest request) {
        authService.requestOtp(request.getPhoneNumber());
        return ResponseEntity.ok("OTP sent");
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {

        String token = authService.VerifyOtp(
                request.getPhoneNumber(),
                request.getOtp()
        );

        return ResponseEntity.ok(
                Map.of(
                        "access_token", token,
                        "expires_in", 86400
                )
        );
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        String token = authHeader.substring(7);
        Date expiry = jwtService.extractExpiration(token);
        long ttl = expiry.getTime() - new Date().getTime();
        tokenBlacklistService.blacklistToken(token, ttl);
        return ResponseEntity.ok("Logged out successfully");
    }
}
