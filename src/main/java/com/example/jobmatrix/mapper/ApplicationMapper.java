package com.example.jobmatrix.mapper;

import com.example.jobmatrix.application.model.Application;
import com.example.jobmatrix.dto.response.ApplicationResponse;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public ApplicationResponse toResponse(
            Application application
    ) {

        return ApplicationResponse.builder()
                .id(application.getId())
                .candidateId(
                        application.getCandidate().getId()
                )
                .candidateName(
                        application.getCandidate()
                                .getUsername()
                )
                .jobId(
                        application.getJob().getId()
                )
                .jobTitle(
                        application.getJob().getTitle()
                )
                .companyName(
                        application.getJob()
                                .getCompany()
                                .getName()
                )
                .resumeUrl(
                        application.getResumeUrl()
                )
                .coverLetter(
                        application.getCoverLetter()
                )
                .status(
                        application.getStatus().name()
                )
                .appliedAt(
                        application.getAppliedAt()
                )
                .build();
    }
}