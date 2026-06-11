package com.example.jobmatrix.job.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "jobs")
public class Job {

    @Id
    private String id;
    @Indexed
    private String recruiterId;
    @Indexed
    private String companyName;
    @TextIndexed
    private String title;
    @TextIndexed
    private String jobDescription;
    @TextIndexed
    private List<String> skills;
    private String eligibility;
    @Indexed
    private String location;
    private Double salary;
    private Integer experienceRequired;
}