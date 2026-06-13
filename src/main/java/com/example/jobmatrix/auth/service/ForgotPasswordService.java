package com.example.jobmatrix.auth.service;

import com.example.jobmatrix.auth.dto.ResetPasswordRequest;
import com.example.jobmatrix.exception.BadRequestException;
import com.example.jobmatrix.notification.service.EmailService;
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

    private final EmailService emailService;

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

    public void sendForgotPasswordOtp(
            String email
    ) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "User not found"
                                )
                        );

        String otp =
                otpService.generateOtp(
                        email
                );

        String html = """
            <html>
            <body>

            <h2>Password Reset OTP</h2>

            <p>Hello %s,</p>

            <p>Your OTP is:</p>

            <h1>%s</h1>

            <p>
            This OTP will expire in
            10 minutes.
            </p>

            </body>
            </html>
            """
                .formatted(
                        user.getUsername(),
                        otp
                );

        emailService.sendEmail(
                email,
                "JobMatrix Password Reset OTP",
                html
        );
    }
}