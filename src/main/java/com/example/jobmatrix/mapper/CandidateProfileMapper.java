package com.example.jobmatrix.mapper;

import com.example.jobmatrix.dto.response.CandidateProfileResponse;
import com.example.jobmatrix.user.model.CandidateProfile;
import org.springframework.stereotype.Component;

@Component
public class CandidateProfileMapper {

    public CandidateProfileResponse toResponse(
            CandidateProfile profile
    ) {

        return CandidateProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .username(profile.getUser().getUsername())
                .email(profile.getUser().getEmail())
                .location(profile.getLocation())
                .experience(profile.getExperience())
                .skills(profile.getSkills())
                .resumeUrl(profile.getResumeUrl())
                .bio(profile.getBio())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .build();
    }
}