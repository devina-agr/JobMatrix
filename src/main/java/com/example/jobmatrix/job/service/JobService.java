package com.example.jobmatrix.job.service;

import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.company.repository.CompanyRepository;
import com.example.jobmatrix.dto.request.CreateJobRequest;
import com.example.jobmatrix.dto.request.UpdateJobRequest;
import com.example.jobmatrix.dto.response.JobResponse;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.job.model.Job;
import com.example.jobmatrix.job.repository.JobRepository;
import com.example.jobmatrix.mapper.JobMapper;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobMapper jobMapper;

    public JobResponse createJob(
            CreateJobRequest request,
            Long recruiterId
    ) {

        User recruiter =
                userRepository.findById(recruiterId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Recruiter not found"
                                )
                        );

        Company company =
                companyRepository.findById(
                                request.getCompanyId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Company not found"
                                )
                        );

        Job job = new Job();

        job.setRecruiter(recruiter);
        job.setCompany(company);
        job.setTitle(request.getTitle());
        job.setJobDescription(
                request.getJobDescription()
        );
        job.setSkills(request.getSkills());
        job.setEligibility(
                request.getEligibility()
        );
        job.setLocation(
                request.getLocation()
        );
        job.setSalary(
                request.getSalary()
        );
        job.setExperienceRequired(
                request.getExperienceRequired()
        );
        job.setJobType(
                request.getJobType()
        );

        jobRepository.save(job);

        return jobMapper.toResponse(job);
    }

    public JobResponse getJob(
            Long jobId
    ) {

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job not found"
                                )
                        );

        return jobMapper.toResponse(job);
    }

    public Page<JobResponse> getAllJobs(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt")
                                .descending()
                );

        return jobRepository
                .findByActiveTrue(pageable)
                .map(jobMapper::toResponse);
    }

    public JobResponse updateJob(
            Long jobId,
            UpdateJobRequest request
    ) {

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job not found"
                                )
                        );

        if(request.getTitle()!=null)
            job.setTitle(request.getTitle());

        if(request.getJobDescription()!=null)
            job.setJobDescription(
                    request.getJobDescription()
            );

        if(request.getLocation()!=null)
            job.setLocation(
                    request.getLocation()
            );

        if(request.getSalary()!=null)
            job.setSalary(
                    request.getSalary()
            );

        if(request.getExperienceRequired()!=null)
            job.setExperienceRequired(
                    request.getExperienceRequired()
            );

        if(request.getEligibility()!=null)
            job.setEligibility(
                    request.getEligibility()
            );

        if(request.getActive() != null) {
            job.setActive(request.getActive());
        }
        if(request.getSkills() != null) {
            job.setSkills(request.getSkills());
        }

        if(request.getJobType() != null) {
            job.setJobType(request.getJobType());
        }
        jobRepository.save(job);

        return jobMapper.toResponse(job);
    }

    public void deleteJob(
            Long jobId
    ) {

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job not found"
                                )
                        );

        jobRepository.delete(job);
    }
}