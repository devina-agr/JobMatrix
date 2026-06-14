package com.example.jobmatrix.user.service;

import com.example.jobmatrix.dto.request.CreateCandidateProfileRequest;
import com.example.jobmatrix.dto.request.UpdateCandidateProfileRequest;
import com.example.jobmatrix.dto.response.CandidateProfileResponse;
import com.example.jobmatrix.dto.response.CloudinaryUploadResponse;
import com.example.jobmatrix.exception.BadRequestException;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.mapper.CandidateProfileMapper;
import com.example.jobmatrix.security.UserPrincipal;
import com.example.jobmatrix.upload.CloudinaryService;
import com.example.jobmatrix.user.model.CandidateProfile;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.CandidateProfileRepository;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;

    private final UserRepository userRepository;

    private final CandidateProfileMapper candidateProfileMapper;

    private final CloudinaryService cloudinaryService;

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
                        .bio(request.getBio())
                        .githubUrl(request.getGithubUrl())
                        .linkedinUrl(request.getLinkedinUrl())
                        .resumeUrl(null)
                        .resumePublicId(null)
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

    public String uploadResume(
            Authentication authentication,
            MultipartFile file
    ) {

        UserPrincipal principal =
                (UserPrincipal)
                        authentication.getPrincipal();

        CandidateProfile profile =
                candidateProfileRepository
                        .findByUserId(
                                principal.getId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Profile not found"
                                )
                        );

        if(profile.getResumePublicId() != null){

            cloudinaryService.deleteFile(
                    profile.getResumePublicId()
            );
        }

        CloudinaryUploadResponse upload =
                cloudinaryService.uploadResume(
                        file
                );

        profile.setResumeUrl(
                upload.getUrl()
        );

        profile.setResumePublicId(
                upload.getPublicId()
        );

        candidateProfileRepository.save(
                profile
        );

        return upload.getUrl();
    }

    public void deleteResume(
            Authentication authentication
    ) {

        UserPrincipal principal =
                (UserPrincipal)
                        authentication.getPrincipal();

        CandidateProfile profile =
                candidateProfileRepository
                        .findByUserId(
                                principal.getId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Profile not found"
                                )
                        );

        if(profile.getResumePublicId() != null){

            cloudinaryService.deleteFile(
                    profile.getResumePublicId()
            );

            profile.setResumeUrl(null);

            profile.setResumePublicId(null);

            candidateProfileRepository.save(
                    profile
            );
        }
    }
}