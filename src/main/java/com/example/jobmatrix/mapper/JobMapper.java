package com.example.jobmatrix.mapper;

import com.example.jobmatrix.dto.response.JobResponse;
import com.example.jobmatrix.job.model.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public JobResponse toResponse(Job job) {

        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .companyName(job.getCompany().getName())
                .location(job.getLocation())
                .salary(job.getSalary())
                .experienceRequired(
                        job.getExperienceRequired()
                )
                .skills(job.getSkills())
                .build();
    }
}