package com.example.jobmatrix.user.controller;

import com.example.jobmatrix.security.UserPrincipal;
import com.example.jobmatrix.user.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final CandidateProfileService candidateProfileService;

    @GetMapping("/resume/{candidateId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<byte[]> downloadResume(
            @PathVariable Long candidateId
    ) {

        byte[] file =
                candidateProfileService
                        .downloadResumeBytes(
                                candidateId
                        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=resume.pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(file);
    }

    @GetMapping("/resume/download")
    public ResponseEntity<byte[]> downloadMyResume(
            Authentication authentication
    ) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        byte[] file =
                candidateProfileService.downloadResumeBytes(
                        principal.getId()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=resume.pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(file);
    }
}
