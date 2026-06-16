package com.example.jobmatrix.explore.provider;

import com.example.jobmatrix.explore.dto.ExternalJobDto;
import com.example.jobmatrix.explore.dto.JobSearchRequest;

import java.util.List;

public interface JobProvider {

    List<ExternalJobDto> searchJobs(
            JobSearchRequest request
    );
}