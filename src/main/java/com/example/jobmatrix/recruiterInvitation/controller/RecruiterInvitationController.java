package com.example.jobmatrix.recruiterInvitation.controller;

import com.example.jobmatrix.dto.request.CreateRecruiterInvitationRequest;
import com.example.jobmatrix.dto.response.RecruiterInvitationResponse;
import com.example.jobmatrix.recruiterInvitation.service.RecruiterInvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company-manager/invitations")
@RequiredArgsConstructor
public class RecruiterInvitationController {

    private final RecruiterInvitationService service;

    @PostMapping
    public ResponseEntity<RecruiterInvitationResponse>
    inviteRecruiter(
            @RequestParam Long managerId,
            @Valid
            @RequestBody
            CreateRecruiterInvitationRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        service.inviteRecruiter(
                                managerId,
                                request
                        )
                );
    }
}