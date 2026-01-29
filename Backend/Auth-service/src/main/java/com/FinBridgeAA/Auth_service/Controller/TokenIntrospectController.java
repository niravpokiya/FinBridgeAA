package com.FinBridgeAA.Auth_service.Controller;

import com.FinBridgeAA.Auth_service.DTO.TokenIntrospectResponse;
import com.FinBridgeAA.Auth_service.Services.AuthService;
import com.FinBridgeAA.Auth_service.Services.JwtService;
import com.FinBridgeAA.Auth_service.Services.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/token")
public class TokenIntrospectController {
    private final TokenBlacklistService tokenBlacklistService;
    private final AuthService authService;
    private final JwtService jwtService;

    public TokenIntrospectController(TokenBlacklistService tokenBlacklistService, AuthService authService,
            JwtService jwtService) {
        this.tokenBlacklistService = tokenBlacklistService;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/introspect")
    public ResponseEntity<TokenIntrospectResponse> introspect(@RequestHeader("Authorization") String authHeader) {

        System.out.println("introspect hit");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(
                    new TokenIntrospectResponse(false, null, null, "INVALID_HEADER"));
        }
        String token = authHeader.substring(7);
        if (tokenBlacklistService.isBlacklisted(token)) {
            return ResponseEntity.ok(
                    new TokenIntrospectResponse(true, null, null, "TOKEN_BLACKLISTED"));
        }
        return ResponseEntity.ok(
                new TokenIntrospectResponse(
                        true,
                        jwtService.extractUserId(token),
                        jwtService.extractRole(token),
                        null));
    }
}
