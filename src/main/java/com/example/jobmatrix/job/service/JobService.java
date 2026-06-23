package com.example.jobmatrix.job.service;

import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.company.repository.CompanyRepository;
import com.example.jobmatrix.dto.request.CreateJobRequest;
import com.example.jobmatrix.dto.request.UpdateJobRequest;
import com.example.jobmatrix.dto.response.JobResponse;
import com.example.jobmatrix.exception.BadRequestException;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.exception.UnauthorizedException;
import com.example.jobmatrix.job.model.Job;
import com.example.jobmatrix.job.repository.JobRepository;
import com.example.jobmatrix.mapper.JobMapper;
import com.example.jobmatrix.user.model.RecruiterProfile;
import com.example.jobmatrix.user.model.Role;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.RecruiterProfileRepository;
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
    private final RecruiterProfileRepository recruiterProfileRepository;
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
        if (!recruiter.isEnabled()) {
            throw new UnauthorizedException(
                    "Account is disabled"
            );
        }
        Company company;

        if (recruiter.getRole() == Role.ROLE_COMPANY_MANAGER) {

            company =
                    companyRepository.findByManager(recruiter)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Company not found"
                                    )
                            );

        } else {

            RecruiterProfile profile =
                    recruiterProfileRepository
                            .findByUser(recruiter)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Recruiter profile not found"
                                    )
                            );

            company = profile.getCompany();
        }
        if (!company.isVerified()) {
            throw new IllegalStateException(
                    "Company must be verified before posting jobs"
            );
        }
        if (company.isBlocked()) {
            throw new BadRequestException(
                    "Company is blocked by admin"
            );
        }

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
        if (!job.isActive()) {
            throw new ResourceNotFoundException(
                    "Job not found"
            );
        }
        if (job.getCompany().isBlocked()) {
            throw new ResourceNotFoundException(
                    "Job is not active"
            );
        }
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
                .findByActiveTrueAndCompanyBlockedFalse(
                        pageable
                )
                .map(jobMapper::toResponse);
    }

    public JobResponse updateJob(
            Long jobId,
            UpdateJobRequest request,
            Long currentUserId) {

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job not found"
                                )
                        );
        if (job.getCompany().isBlocked()) {
            throw new BadRequestException(
                    "Company is blocked"
            );
        }
        if (!job.isActive()) {
            throw new BadRequestException(
                    "Cannot update an inactive job"
            );
        }
        User currentUser =
                userRepository.findById(currentUserId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        Company company = job.getCompany();

        boolean allowed = false;

        if (currentUser.getRole() == Role.ROLE_COMPANY_MANAGER) {

            allowed =
                    company.getManager()
                            .getId()
                            .equals(currentUserId);

        }
        else if (currentUser.getRole() == Role.ROLE_RECRUITER) {

            RecruiterProfile profile =
                    recruiterProfileRepository
                            .findByUser(currentUser)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Recruiter profile not found"
                                    )
                            );

            allowed =
                    profile.getCompany()
                            .getId()
                            .equals(company.getId());
        }

        if (!allowed) {
            throw new UnauthorizedException(
                    "Access denied"
            );
        }

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

        if(request.getSkills() != null) {
            job.setSkills(request.getSkills());
        }

        if(request.getJobType() != null) {
            job.setJobType(request.getJobType());
        }
        jobRepository.save(job);

        return jobMapper.toResponse(job);
    }

    public void deactivateJob(
            Long jobId,
            Long currentUserId) {

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job not found"
                                )
                        );
        if (job.getCompany().isBlocked()) {
            throw new BadRequestException(
                    "Company is blocked"
            );
        }
        User currentUser =
                userRepository.findById(currentUserId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        Company company = job.getCompany();

        boolean allowed = false;

        if (currentUser.getRole() == Role.ROLE_COMPANY_MANAGER) {

            allowed =
                    company.getManager()
                            .getId()
                            .equals(currentUserId);

        }
        else if (currentUser.getRole() == Role.ROLE_RECRUITER) {

            RecruiterProfile profile =
                    recruiterProfileRepository
                            .findByUser(currentUser)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Recruiter profile not found"
                                    )
                            );

            allowed =
                    profile.getCompany()
                            .getId()
                            .equals(company.getId());
        }

        if (!allowed) {
            throw new UnauthorizedException(
                    "Access denied"
            );
        }
        job.setActive(false);
        jobRepository.save(job);
    }

    public Page<JobResponse> searchJobs(
            String keyword,
            String location,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        Page<Job> jobs;

        if(keyword != null && !keyword.isBlank()
                && location != null && !location.isBlank()) {

            jobs =
                    jobRepository
                            .findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCaseAndActiveTrueAndCompanyBlockedFalse(
                                    keyword,
                                    location,
                                    pageable
                            );

        } else if(keyword != null && !keyword.isBlank()) {

            jobs =
                    jobRepository
                            .findByTitleContainingIgnoreCaseAndActiveTrueAndCompanyBlockedFalse(
                                    keyword,
                                    pageable
                            );

        } else if(location != null && !location.isBlank()) {

            jobs =
                    jobRepository
                            .findByLocationContainingIgnoreCaseAndActiveTrueAndCompanyBlockedFalse(
                                    location,
                                    pageable
                            );

        } else {

            jobs =
                    jobRepository.findByActiveTrueAndCompanyBlockedFalse(
                            pageable
                    );
        }

        return jobs.map(jobMapper::toResponse);
    }
}