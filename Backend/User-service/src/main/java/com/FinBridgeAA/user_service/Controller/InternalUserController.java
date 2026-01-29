package com.FinBridgeAA.user_service.Controller;

import com.FinBridgeAA.user_service.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/user")
public class InternalUserController {
    private final UserService service;
    public InternalUserController(UserService service) {
        this.service = service;
    }
    @PostMapping("/create")
    public ResponseEntity<String> createUser(
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        service.createUserIfNotExists(userId);
        return ResponseEntity.ok("User created");
    }
}