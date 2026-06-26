package com.example.jobmatrix.recruiterInvitation.service;

import com.example.jobmatrix.auth.model.RefreshToken;
import com.example.jobmatrix.auth.repository.RefreshTokenRepository;
import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.company.repository.CompanyRepository;
import com.example.jobmatrix.dto.TokenPayload;
import com.example.jobmatrix.dto.request.AcceptInvitationRequest;
import com.example.jobmatrix.dto.request.CreateRecruiterInvitationRequest;
import com.example.jobmatrix.dto.response.AuthResponse;
import com.example.jobmatrix.dto.response.RecruiterInvitationResponse;
import com.example.jobmatrix.notification.service.EmailService;
import com.example.jobmatrix.exception.BadRequestException;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.recruiterInvitation.model.RecruiterInvitation;
import com.example.jobmatrix.recruiterInvitation.repository.RecruiterInvitationRepository;
import com.example.jobmatrix.security.JwtService;
import com.example.jobmatrix.user.model.RecruiterProfile;
import com.example.jobmatrix.user.model.Role;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.RecruiterProfileRepository;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecruiterInvitationService {

    private final RecruiterInvitationRepository invitationRepository;

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private final RecruiterProfileRepository recruiterProfileRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final RefreshTokenRepository refreshTokenRepository;

    private final EmailService emailService;

    public RecruiterInvitationResponse inviteRecruiter(
            Long managerId,
            CreateRecruiterInvitationRequest request
    ) {

        User manager =
                userRepository.findById(managerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Manager not found"
                                ));

        Company company =
                companyRepository.findByManager(manager)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Company not found"
                                ));

        if (!company.isVerified()) {

            throw new BadRequestException(
                    "Company is not verified"
            );
        }

        if (invitationRepository
                .existsByEmailAndCompanyId(
                        request.getEmail(),
                        company.getId()
                )) {

            throw new BadRequestException(
                    "Invitation already sent"
            );
        }

        String token =
                UUID.randomUUID().toString();

        RecruiterInvitation invitation =
                RecruiterInvitation.builder()
                        .email(
                                request.getEmail()
                        )
                        .company(
                                company
                        )
                        .invitedBy(
                                manager
                        )
                        .token(
                                token
                        )
                        .accepted(false)
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusDays(7)
                        )
                        .build();

        invitationRepository.save(
                invitation
        );

        String inviteLink =
                "https://job-matrix-frontend.vercel.app/accept-invite?token="
                        + token;

        emailService.sendRecruiterInvitation(
                request.getEmail(),
                request.getEmail(),
                company.getName(),
                inviteLink
        );

        return RecruiterInvitationResponse.builder()
                .id(
                        invitation.getId()
                )
                .email(
                        invitation.getEmail()
                )
                .companyName(
                        company.getName()
                )
                .token(
                        invitation.getToken()
                )
                .accepted(false)
                .expiresAt(
                        invitation.getExpiresAt()
                )
                .build();
    }

    public AuthResponse acceptInvitation(
            AcceptInvitationRequest request
    ) {

        RecruiterInvitation invitation =
                invitationRepository
                        .findByToken(
                                request.getToken()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Invitation not found"
                                ));

        if (invitation.isAccepted()) {

            throw new BadRequestException(
                    "Invitation already used"
            );
        }

        if (
                invitation.getExpiresAt()
                        .isBefore(
                                LocalDateTime.now()
                        )
        ) {

            throw new BadRequestException(
                    "Invitation expired"
            );
        }

        if (
                userRepository.existsByEmail(
                        invitation.getEmail()
                )
        ) {

            throw new BadRequestException(
                    "User already exists"
            );
        }

        User recruiter =
                User.builder()
                        .username(
                                request.getUsername()
                        )
                        .email(
                                invitation.getEmail()
                        )
                        .password(
                                passwordEncoder.encode(
                                        request.getPassword()
                                )
                        )
                        .role(
                                Role.ROLE_RECRUITER
                        )

                        .enabled(true)
                        .tokenVersion(0)
                        .build();

        userRepository.save(
                recruiter
        );

        RecruiterProfile recruiterProfile =
                RecruiterProfile.builder()
                        .user(
                                recruiter
                        )
                        .company(
                                invitation.getCompany()
                        )
                        .department(request.getDepartment())
                        .verified(true)
                        .build();

        recruiterProfileRepository.save(
                recruiterProfile
        );

        invitation.setAccepted(true);

        invitationRepository.save(
                invitation
        );

        TokenPayload payload =
                new TokenPayload(
                        recruiter.getEmail(),
                        recruiter.getTokenVersion()
                );

        String accessToken =
                jwtService.generateAccessToken(
                        payload
                );

        String refreshToken =
                jwtService.generateRefreshToken(
                        payload
                );

        RefreshToken refreshTokenEntity =
                RefreshToken.builder()
                        .user(
                                recruiter
                        )
                        .token(
                                refreshToken
                        )
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusDays(7)
                        )
                        .revoked(false)
                        .build();

        refreshTokenRepository.save(
                refreshTokenEntity
        );

        return AuthResponse.builder()
                .accessToken(
                        accessToken
                )
                .refreshToken(
                        refreshToken
                )
                .email(
                        recruiter.getEmail()
                )
                .role(
                        recruiter.getRole().name()
                )
                .build();
    }

    public void deleteExpiredInvitations() {

        invitationRepository.deleteByExpiresAtBefore(
                LocalDateTime.now()
        );
    }
}