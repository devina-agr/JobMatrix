package com.example.jobmatrix.user.controller;

import com.example.jobmatrix.dto.request.CreateCandidateProfileRequest;
import com.example.jobmatrix.dto.request.UpdateCandidateProfileRequest;
import com.example.jobmatrix.dto.response.CandidateProfileResponse;
import com.example.jobmatrix.security.UserPrincipal;
import com.example.jobmatrix.user.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate/profile")
@RequiredArgsConstructor
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    @PostMapping
    public ResponseEntity<CandidateProfileResponse>
    createProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody CreateCandidateProfileRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        candidateProfileService.createProfile(
                                principal.getId(),
                                request
                        )
                );
    }

    @GetMapping
    public ResponseEntity<CandidateProfileResponse>
    getProfile(
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ResponseEntity.ok(
                candidateProfileService.getProfile(
                        principal.getId()
                )
        );
    }

    @PutMapping
    public ResponseEntity<CandidateProfileResponse>
    updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateCandidateProfileRequest request
    ) {

        return ResponseEntity.ok(
                candidateProfileService.updateProfile(
                        principal.getId(),
                        request
                )
        );
    }
}