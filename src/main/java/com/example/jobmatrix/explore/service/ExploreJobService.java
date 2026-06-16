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

        int page =
                request.getPage();

        int size =
                request.getSize();

        int start =
                page * size;

        int end =
                Math.min(
                        start + size,
                        jobs.size()
                );

        List<ExternalJobDto> paginatedJobs =
                start >= jobs.size()
                        ? new ArrayList<>()
                        : new ArrayList<>(
                        jobs.subList(
                                start,
                                end
                        )
                );

        return JobSearchResponse.builder()
                .totalJobs(
                        jobs.size()
                )
                .page(page)
                .size(size)
                .jobs(
                        paginatedJobs
                )
                .build();
    }
}