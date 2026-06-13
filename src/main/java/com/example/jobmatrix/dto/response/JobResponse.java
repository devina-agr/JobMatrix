package com.example.jobmatrix.dto.response;

import com.example.jobmatrix.job.model.JobType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {

    private Long id;

    private String title;

    private String companyName;

    private String location;

    private Double salary;

    private Integer experienceRequired;

    private JobType jobType;

    private Set<String> skills;

    private boolean active;

    private LocalDateTime createdAt;
}