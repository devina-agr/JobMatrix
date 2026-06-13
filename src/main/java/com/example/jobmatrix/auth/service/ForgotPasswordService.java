package com.example.jobmatrix.auth.service;

import com.example.jobmatrix.auth.dto.ResetPasswordRequest;
import com.example.jobmatrix.exception.BadRequestException;
import com.example.jobmatrix.otp.OtpService;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;

    private final OtpService otpService;

    private final PasswordEncoder passwordEncoder;

    public void resetPassword(
            ResetPasswordRequest request
    ) {

        if(!otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        )) {

            throw new BadRequestException(
                    "Invalid OTP"
            );
        }

        User user =
                userRepository.findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "User not found"
                                )
                        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        user.setTokenVersion(
                user.getTokenVersion() + 1
        );

        userRepository.save(user);
    }
}