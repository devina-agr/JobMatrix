package com.example.jobmatrix.job.Controller;

import com.example.jobmatrix.dto.request.CreateJobRequest;
import com.example.jobmatrix.dto.request.UpdateJobRequest;
import com.example.jobmatrix.dto.response.JobResponse;
import com.example.jobmatrix.exception.BadRequestException;
import com.example.jobmatrix.job.model.Job;
import com.example.jobmatrix.job.service.JobService;
import com.example.jobmatrix.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize(
            "hasAnyRole('COMPANY_MANAGER','RECRUITER')"
    )
    public ResponseEntity<JobResponse> createJob(
            @RequestBody CreateJobRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        jobService.createJob(
                                request,
                                principal.getId()
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJob(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                jobService.getJob(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<JobResponse>> getJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                jobService.getAllJobs(
                        page,
                        size
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('COMPANY_MANAGER','RECRUITER')"
    )
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @RequestBody UpdateJobRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        return ResponseEntity.ok(
                jobService.updateJob(
                        id,
                        request,
                        userPrincipal.getId()
                )
        );
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize(
            "hasAnyRole('COMPANY_MANAGER','RECRUITER')"
    )
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        jobService.deactivateJob(id,userPrincipal.getId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public")
    public ResponseEntity<Page<JobResponse>> getPublicJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                jobService.getAllJobs(
                        page,
                        size
                )
        );
    }
    @GetMapping("/search")
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                jobService.searchJobs(
                        keyword,
                        location,
                        page,
                        size
                )
        );
    }
}
