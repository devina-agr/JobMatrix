package com.example.jobmatrix.dto.response;

import com.example.jobmatrix.application.model.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {

    private Long id;

    private Long candidateId;

    private String candidateName;

    private Long jobId;

    private String jobTitle;

    private String companyName;

    private String resumeUrl;

    private String coverLetter;

    private ApplicationStatus status;

    private LocalDateTime appliedAt;
}