package com.example.jobmatrix.auth;

import com.example.jobmatrix.auth.model.RefreshToken;
import com.example.jobmatrix.auth.repository.RefreshTokenRepository;
import com.example.jobmatrix.dto.TokenPayload;
import com.example.jobmatrix.dto.request.LoginRequest;
import com.example.jobmatrix.dto.request.RegisterRequest;
import com.example.jobmatrix.dto.response.AuthResponse;
import com.example.jobmatrix.exception.EmailAlreadyExistsException;
import com.example.jobmatrix.exception.InvalidTokenException;
import com.example.jobmatrix.exception.RefreshTokenExpiredException;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.security.JwtService;
import com.example.jobmatrix.user.model.Role;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthResponse register(
            RegisterRequest request
    ) {

        if (userRepository.existsByEmail(
                request.getEmail()
        )) {

            throw new EmailAlreadyExistsException(
                    "Email already exists"
            );
        }

        User user = User.builder()
                .username(
                        request.getUsername()
                )
                .email(
                        request.getEmail()
                )
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(
                        request.getRole() == null
                                ? Role.ROLE_CANDIDATE
                                : request.getRole()
                )
                .enabled(true)
                .tokenVersion(0)
                .build();

        userRepository.save(user);

        return generateTokens(user);
    }

    public AuthResponse login(
            LoginRequest request
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user =
                userRepository.findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                            "User not found"
                                )

                        );

        return generateTokens(user);
    }

    public AuthResponse refreshToken(
            String refreshToken
    ) {

        RefreshToken token =
                refreshTokenRepository
                        .findByToken(refreshToken)
                        .orElseThrow(
                                () -> new InvalidTokenException(
                                        "Invalid refresh token"
                                )
                        );

        if (!token.isValid()) {

            throw new RefreshTokenExpiredException(
                    "Refresh token expired"
            );
        }

        User user = token.getUser();

        TokenPayload payload =
                new TokenPayload(
                        user.getEmail(),
                        user.getTokenVersion()
                );

        String newAccessToken =
                jwtService.generateAccessToken(
                        payload
                );

        return AuthResponse.builder()
                .accessToken(
                        newAccessToken
                )
                .refreshToken(
                        refreshToken
                )
                .email(
                        user.getEmail()
                )
                .role(
                        user.getRole().name()
                )
                .build();
    }

    public void logout(
            String refreshToken
    ) {

        refreshTokenRepository
                .findByToken(refreshToken)
                .ifPresent(token -> {

                    token.setRevoked(true);

                    refreshTokenRepository
                            .save(token);
                });
    }

    private AuthResponse generateTokens(
            User user
    ) {

        TokenPayload payload =
                new TokenPayload(
                        user.getEmail(),
                        user.getTokenVersion()
                );

        String accessToken =
                jwtService.generateAccessToken(
                        payload
                );

        String refreshToken =
                jwtService.generateRefreshToken(
                        payload
                );

        RefreshToken tokenEntity =
                RefreshToken.builder()
                        .token(refreshToken)
                        .user(user)
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusDays(7)
                        )
                        .revoked(false)
                        .build();

        refreshTokenRepository.save(
                tokenEntity
        );

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(
                        user.getRole().name()
                )
                .build();
    }
}