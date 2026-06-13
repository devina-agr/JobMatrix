package com.example.jobmatrix.user.controller;

import com.example.jobmatrix.dto.request.CreateRecruiterProfileRequest;
import com.example.jobmatrix.dto.request.UpdateRecruiterProfileRequest;
import com.example.jobmatrix.dto.response.RecruiterProfileResponse;
import com.example.jobmatrix.security.UserPrincipal;
import com.example.jobmatrix.user.service.RecruiterProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter/profile")
@RequiredArgsConstructor
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;

    @PostMapping
    public ResponseEntity<RecruiterProfileResponse>
    createProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody CreateRecruiterProfileRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        recruiterProfileService.createProfile(
                                principal.getId(),
                                request
                        )
                );
    }

    @GetMapping
    public ResponseEntity<RecruiterProfileResponse>
    getProfile(
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ResponseEntity.ok(
                recruiterProfileService.getProfile(
                        principal.getId()
                )
        );
    }

    @PutMapping
    public ResponseEntity<RecruiterProfileResponse>
    updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateRecruiterProfileRequest request
    ) {

        return ResponseEntity.ok(
                recruiterProfileService.updateProfile(
                        principal.getId(),
                        request
                )
        );
    }
}