package com.example.jobmatrix.user.service;

import com.example.jobmatrix.dto.request.CreateCandidateProfileRequest;
import com.example.jobmatrix.dto.request.UpdateCandidateProfileRequest;
import com.example.jobmatrix.dto.response.CandidateProfileResponse;
import com.example.jobmatrix.exception.BadRequestException;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.mapper.CandidateProfileMapper;
import com.example.jobmatrix.user.model.CandidateProfile;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.CandidateProfileRepository;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;

    private final UserRepository userRepository;

    private final CandidateProfileMapper candidateProfileMapper;

    public CandidateProfileResponse createProfile(
            Long userId,
            CreateCandidateProfileRequest request
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        if(candidateProfileRepository.existsByUser(user)) {

            throw new BadRequestException(
                    "Candidate profile already exists"
            );
        }

        CandidateProfile profile =
                CandidateProfile.builder()
                        .user(user)
                        .location(request.getLocation())
                        .experience(request.getExperience())
                        .skills(request.getSkills())
                        .resumeUrl(request.getResumeUrl())
                        .bio(request.getBio())
                        .githubUrl(request.getGithubUrl())
                        .linkedinUrl(request.getLinkedinUrl())
                        .build();

        candidateProfileRepository.save(profile);

        return candidateProfileMapper.toResponse(profile);
    }

    public CandidateProfileResponse getProfile(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        CandidateProfile profile =
                candidateProfileRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"
                                )
                        );

        return candidateProfileMapper.toResponse(
                profile
        );
    }

    public CandidateProfileResponse updateProfile(
            Long userId,
            UpdateCandidateProfileRequest request
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        CandidateProfile profile =
                candidateProfileRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"
                                )
                        );

        if(request.getLocation()!=null)
            profile.setLocation(
                    request.getLocation()
            );

        if(request.getExperience()!=null)
            profile.setExperience(
                    request.getExperience()
            );

        if(request.getSkills()!=null)
            profile.setSkills(
                    request.getSkills()
            );

        if(request.getResumeUrl()!=null)
            profile.setResumeUrl(
                    request.getResumeUrl()
            );

        if(request.getBio()!=null)
            profile.setBio(
                    request.getBio()
            );

        if(request.getGithubUrl()!=null)
            profile.setGithubUrl(
                    request.getGithubUrl()
            );

        if(request.getLinkedinUrl()!=null)
            profile.setLinkedinUrl(
                    request.getLinkedinUrl()
            );

        candidateProfileRepository.save(
                profile
        );

        return candidateProfileMapper.toResponse(
                profile
        );
    }
}