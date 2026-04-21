package com.example.librarymanage_be.service;

import com.example.librarymanage_be.dto.request.LoginRequest;
import com.example.librarymanage_be.dto.request.RegisterRequest;
import com.example.librarymanage_be.dto.response.AuthResponse;
import com.example.librarymanage_be.dto.response.LoginResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);
}
