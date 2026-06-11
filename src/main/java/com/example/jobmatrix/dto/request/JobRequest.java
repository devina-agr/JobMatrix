package com.example.jobmatrix.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class JobRequest {

    private String title;
    private String description;
    private List<String> skills;
    private String location;
    private String jobType;
    private String eligibility;
    private Double salary;
    private Integer experienceRequired;
}
