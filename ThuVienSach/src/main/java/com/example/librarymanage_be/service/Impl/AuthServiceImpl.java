package com.example.librarymanage_be.service.Impl;

import com.example.librarymanage_be.dto.request.LoginRequest;
import com.example.librarymanage_be.dto.request.RegisterRequest;
import com.example.librarymanage_be.dto.response.AuthResponse;
import com.example.librarymanage_be.dto.response.LoginResponse;
import com.example.librarymanage_be.entity.Roles;
import com.example.librarymanage_be.entity.UserRoleId;
import com.example.librarymanage_be.entity.Users;
import com.example.librarymanage_be.entity.UserRole;
import com.example.librarymanage_be.repo.RoleRepository;
import com.example.librarymanage_be.repo.UserRepository;
import com.example.librarymanage_be.service.AuthService;
import com.example.librarymanage_be.service.auth.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final String DEFAULT_ROLE = "ROLE_USER";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        String email = normalizeEmail(registerRequest.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        Users user = new Users();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(email);
        user.setPhone(registerRequest.getPhone());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        //gán role
        Roles role = getDefaultRole();

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        user.getUserRoles().add(userRole);

        UserRoleId id = new UserRoleId();
        id.setUserId(user.getUserId());
        id.setRoleId(role.getRoleId());

        userRole.setId(id);
        userRepository.save(user);
        return new AuthResponse("Register success");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        ));
        Users user = userRepository.findByEmail(loginRequest.getEmail());
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        return new LoginResponse(accessToken,refreshToken);
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        return email.trim().toLowerCase();
    }

    private Roles getDefaultRole() {
        return roleRepository.findByRoleName(DEFAULT_ROLE)
                .orElseGet(() -> {
                    Roles role = new Roles();
                    role.setRoleName(DEFAULT_ROLE);
                    return roleRepository.save(role);
                });
    }
}
