package com.example.jobmatrix.recruiterInvitation.controller;

import com.example.jobmatrix.dto.request.AcceptInvitationRequest;
import com.example.jobmatrix.dto.request.CreateRecruiterInvitationRequest;
import com.example.jobmatrix.dto.response.AuthResponse;
import com.example.jobmatrix.dto.response.RecruiterInvitationResponse;
import com.example.jobmatrix.recruiterInvitation.service.RecruiterInvitationService;
import com.example.jobmatrix.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class RecruiterInvitationController {

    private final RecruiterInvitationService service;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_MANAGER')")
    public ResponseEntity<RecruiterInvitationResponse> inviteRecruiter(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateRecruiterInvitationRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        service.inviteRecruiter(
                                principal.getId(),
                                request
                        )
                );
    }

    @PostMapping("/accept")
    public ResponseEntity<AuthResponse> acceptInvitation(
            @Valid @RequestBody AcceptInvitationRequest request
    ) {

        return ResponseEntity.ok(
                service.acceptInvitation(request)
        );
    }
}