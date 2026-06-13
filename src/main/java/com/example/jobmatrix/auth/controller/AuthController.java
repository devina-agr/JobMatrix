package com.example.jobmatrix.auth.controller;

import com.example.jobmatrix.auth.dto.LoginRequest;
import com.example.jobmatrix.auth.service.AuthService;
import com.example.jobmatrix.dto.request.RefreshTokenRequest;
import com.example.jobmatrix.auth.dto.RegisterRequest;
import com.example.jobmatrix.dto.request.RegisterCompanyManagerRequest;
import com.example.jobmatrix.dto.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        authService.register(request)
                );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                authService.refreshToken(
                        request.getRefreshToken()
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody RefreshTokenRequest request
    ) {

        authService.logout(
                request.getRefreshToken()
        );

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/register/company-manager")
    public ResponseEntity<AuthResponse> registerRecruiter(
            @Valid @RequestBody RegisterCompanyManagerRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerCompanyManagerRecruiter(request));
    }
}