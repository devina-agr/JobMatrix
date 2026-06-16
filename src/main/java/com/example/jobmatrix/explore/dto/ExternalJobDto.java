package com.example.jobmatrix.explore.dto;

import com.example.jobmatrix.job.model.JobType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalJobDto implements Serializable {

    private String id;

    private String title;

    private String company;

    private String location;

    private String description;

    private String applyUrl;

    private List<String> skills;

    private Integer experience;

    private Double salary;

    private JobType jobType;

    private Integer matchScore;
}