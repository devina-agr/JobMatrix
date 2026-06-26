package com.example.jobmatrix.application.controller;

import com.example.jobmatrix.application.service.ApplicationService;
import com.example.jobmatrix.dto.request.ApplyJobRequest;
import com.example.jobmatrix.dto.request.UpdateApplicationStatusRequest;
import com.example.jobmatrix.dto.response.ApplicationResponse;
import com.example.jobmatrix.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/jobs/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> applyForJob(
            @PathVariable Long jobId,
            @Valid @RequestBody ApplyJobRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        applicationService.apply(
                                principal.getId(),
                                jobId,
                                request
                        )
                );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<ApplicationResponse>>
    myApplications(
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ResponseEntity.ok(
                applicationService.getCandidateApplications(
                        principal.getId()
                )
        );
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'COMPANY_MANAGER')")
    public ResponseEntity<List<ApplicationResponse>>
    getJobApplications(
            @PathVariable Long jobId
    ) {

        return ResponseEntity.ok(
                applicationService.getJobApplications(
                        jobId
                )
        );
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasAnyRole('RECRUITER', 'COMPANY_MANAGER')")
    public ResponseEntity<ApplicationResponse>
    updateStatus(
            @PathVariable Long applicationId,
            @RequestBody UpdateApplicationStatusRequest request
    ) {

        return ResponseEntity.ok(
                applicationService.updateStatus(
                        applicationId,
                        request
                )
        );
    }

    @GetMapping("/{applicationId}")
    @PreAuthorize("hasAnyRole('RECRUITER','COMPANY_MANAGER')")
    public ResponseEntity<ApplicationResponse>
    getApplication(
            @PathVariable Long applicationId
    ) {

        return ResponseEntity.ok(
                applicationService.getApplication(
                        applicationId
                )
        );
    }
}