package com.example.jobmatrix.dto.request;

import com.example.jobmatrix.job.model.JobType;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateJobRequest {

    private String title;

    private String jobDescription;

    private Set<String> skills;

    private String eligibility;

    private String location;

    private Double salary;

    private Integer experienceRequired;

    private JobType jobType;

    private Boolean active;
}