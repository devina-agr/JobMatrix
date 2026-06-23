package com.example.jobmatrix.dto.request;

import com.example.jobmatrix.job.model.JobType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String jobDescription;

    @NotEmpty
    private Set<String> skills;

    @NotBlank
    private String location;

    @NotNull
    private Double salary;

    @NotNull
    private Integer experienceRequired;

    @NotNull
    private JobType jobType;

    private String eligibility;

}