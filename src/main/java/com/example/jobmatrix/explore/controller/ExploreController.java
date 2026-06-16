package com.example.jobmatrix.explore.controller;

import com.example.jobmatrix.explore.dto.JobSearchRequest;
import com.example.jobmatrix.explore.dto.JobSearchResponse;
import com.example.jobmatrix.explore.service.ExploreJobService;
import com.example.jobmatrix.job.model.JobType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/explore")
@RequiredArgsConstructor
public class ExploreController {

    private final ExploreJobService service;

    @GetMapping("/jobs")
    public JobSearchResponse searchJobs(

            @RequestParam(required = false)
            List<String> skills,

            @RequestParam(required = false)
            String location,

            @RequestParam(required = false)
            Integer experience,

            @RequestParam(required = false)
            Double salary,

            @RequestParam(required = false)
            JobType jobType,

            @RequestParam(defaultValue = "0")
                    Integer page,

            @RequestParam(defaultValue = "10")
            Integer size
    ) {

        JobSearchRequest request =
                new JobSearchRequest();

        request.setSkills(skills);
        request.setLocation(location);
        request.setExperience(experience);
        request.setExpectedSalary(salary);
        request.setJobType(jobType);
        request.setPage(page);
        request.setSize(size);

        return service.searchJobs(
                request
        );
    }
}