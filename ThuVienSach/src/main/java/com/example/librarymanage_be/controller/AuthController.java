package com.example.librarymanage_be.controller;

import com.example.librarymanage_be.dto.request.LoginRequest;
import com.example.librarymanage_be.dto.request.RegisterRequest;
import com.example.librarymanage_be.dto.response.AuthResponse;
import com.example.librarymanage_be.dto.response.LoginResponse;
import com.example.librarymanage_be.service.AuthService;
import com.example.librarymanage_be.service.auth.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody String refreshToken) {
        String email = jwtService.extractEmail(refreshToken);
        // check type = refresh
        if (!jwtService.extractType(refreshToken).equals("refresh")) {
            return ResponseEntity.status(401).build();
        }
        // tạo access token mới
        String newAccessToken = jwtService.generateAccessToken(email);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
