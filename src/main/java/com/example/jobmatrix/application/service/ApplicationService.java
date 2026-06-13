package com.example.jobmatrix.application.service;

import com.example.jobmatrix.application.model.Application;
import com.example.jobmatrix.application.repository.ApplicationRepository;
import com.example.jobmatrix.dto.request.ApplyJobRequest;
import com.example.jobmatrix.dto.request.UpdateApplicationStatusRequest;
import com.example.jobmatrix.dto.response.ApplicationResponse;
import com.example.jobmatrix.exception.JobAlreadyAppliedException;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.job.model.Job;
import com.example.jobmatrix.job.repository.JobRepository;
import com.example.jobmatrix.mapper.ApplicationMapper;
import com.example.jobmatrix.notification.model.NotificationType;
import com.example.jobmatrix.notification.service.NotificationService;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationMapper applicationMapper;
    private final NotificationService notificationService;

    public ApplicationResponse apply(
            Long candidateId,
            Long jobId,
            ApplyJobRequest request
    ) {

        User candidate =
                userRepository.findById(candidateId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate not found"
                                )
                        );

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job not found"
                                )
                        );

        if (applicationRepository.existsByCandidateAndJob(
                candidate,
                job
        )) {

            throw new JobAlreadyAppliedException(
                    "You already applied for this job"
            );
        }

        Application application =
                Application.builder()
                        .candidate(candidate)
                        .job(job)
                        .resumeUrl(
                                request.getResumeUrl()
                        )
                        .coverLetter(
                                request.getCoverLetter()
                        )
                        .build();

        applicationRepository.save(application);

        notificationService.createNotification(
                job.getRecruiter(),
                NotificationType.JOB_APPLIED,
                candidate.getUsername()
                        + " applied for "
                        + job.getTitle()
        );

        return applicationMapper.toResponse(
                application
        );
    }

    public ApplicationResponse getApplication(
            Long applicationId
    ) {

        Application application =
                applicationRepository.findById(
                                applicationId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found"
                                )
                        );

        return applicationMapper.toResponse(
                application
        );
    }

    public List<ApplicationResponse> getCandidateApplications(
            Long candidateId
    ) {

        User candidate =
                userRepository.findById(candidateId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate not found"
                                )
                        );

        return applicationRepository
                .findByCandidate(candidate)
                .stream()
                .map(applicationMapper::toResponse)
                .toList();
    }

    public List<ApplicationResponse> getJobApplications(
            Long jobId
    ) {

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job not found"
                                )
                        );

        return applicationRepository
                .findByJob(job)
                .stream()
                .map(applicationMapper::toResponse)
                .toList();
    }

    public ApplicationResponse updateStatus(
            Long applicationId,
            UpdateApplicationStatusRequest request
    ) {

        Application application =
                applicationRepository.findById(
                                applicationId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found"
                                )
                        );

        application.setStatus(
                request.getStatus()
        );

        applicationRepository.save(
                application
        );

        notificationService.createNotification(
                application.getCandidate(),
                NotificationType.APPLICATION_REVIEWING,
                "Application status updated to "
                        + request.getStatus()
        );

        return applicationMapper.toResponse(
                application
        );
    }
}