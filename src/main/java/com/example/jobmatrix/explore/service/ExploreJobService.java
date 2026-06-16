package com.example.jobmatrix.explore.service;

import com.example.jobmatrix.explore.dto.ExternalJobDto;
import com.example.jobmatrix.explore.dto.JobSearchRequest;
import com.example.jobmatrix.explore.dto.JobSearchResponse;
import com.example.jobmatrix.explore.provider.JobProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExploreJobService {

    private final List<JobProvider> providers;

    @Cacheable(
            value = "job-search",
            key = "#request.cacheKey()",
            unless = "#result == null || #result.jobs.isEmpty()"
    )
    public JobSearchResponse searchJobs(
            JobSearchRequest request
    ) {

        List<ExternalJobDto> jobs =
                new ArrayList<>();

        for (JobProvider provider : providers) {

            jobs.addAll(
                    provider.searchJobs(request)
            );
        }

        return JobSearchResponse.builder()
                .totalJobs(
                        jobs.size()
                )
                .page(
                        request.getPage()
                )
                .size(
                        request.getSize()
                )
                .jobs(
                        jobs
                )
                .build();
    }
}