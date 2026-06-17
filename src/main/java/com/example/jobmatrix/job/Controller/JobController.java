package com.example.jobmatrix.job.Controller;

import com.example.jobmatrix.dto.request.CreateJobRequest;
import com.example.jobmatrix.dto.request.UpdateJobRequest;
import com.example.jobmatrix.dto.response.JobResponse;
import com.example.jobmatrix.job.service.JobService;
import com.example.jobmatrix.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
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
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @RequestBody UpdateJobRequest request
    ) {

        return ResponseEntity.ok(
                jobService.updateJob(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id
    ) {

        jobService.deleteJob(id);

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
}
