package com.example.jobmatrix.recommendation.service;

import com.example.jobmatrix.dto.response.JobResponse;
import com.example.jobmatrix.job.repository.JobRepository;
import com.example.jobmatrix.mapper.JobMapper;
import com.example.jobmatrix.user.model.CandidateProfile;
import com.example.jobmatrix.user.repository.CandidateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final CandidateProfileRepository
            candidateProfileRepository;

    private final JobRepository jobRepository;

    private final JobMapper jobMapper;

    public List<JobResponse> recommendJobs(
            Long userId
    ) {

        CandidateProfile profile =
                candidateProfileRepository
                        .findByUserId(userId)
                        .orElseThrow();

        return jobRepository
                .recommendJobs(
                        profile.getSkills()
                )
                .stream()
                .map(jobMapper::toResponse)
                .toList();
    }
}