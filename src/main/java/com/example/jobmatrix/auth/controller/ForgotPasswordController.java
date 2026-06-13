package com.example.jobmatrix.auth.controller;

import com.example.jobmatrix.auth.dto.ResetPasswordRequest;
import com.example.jobmatrix.auth.service.ForgotPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email
    ) {

        forgotPasswordService.sendForgotPasswordOtp(
                email
        );

        return ResponseEntity.ok(
                "OTP sent successfully"
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid
            @RequestBody
            ResetPasswordRequest request
    ) {

        forgotPasswordService.resetPassword(
                request
        );

        return ResponseEntity.ok(
                "Password reset successfully"
        );
    }
}