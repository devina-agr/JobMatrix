package com.example.jobmatrix.recommendation.controller;

import com.example.jobmatrix.dto.response.JobResponse;
import com.example.jobmatrix.recommendation.service.RecommendationService;
import com.example.jobmatrix.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>>
    recommendJobs(
            @AuthenticationPrincipal
            UserPrincipal principal
    ) {

        return ResponseEntity.ok(
                recommendationService.recommendJobs(
                        principal.getId()
                )
        );
    }
}