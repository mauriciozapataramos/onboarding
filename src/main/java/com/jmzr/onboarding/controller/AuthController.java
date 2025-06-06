package com.jmzr.onboarding.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jmzr.onboarding.dto.AuthRequest;
import com.jmzr.onboarding.dto.AuthResponse;
import com.jmzr.onboarding.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

