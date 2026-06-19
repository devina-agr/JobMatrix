package com.example.jobmatrix.user.service;

import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.company.repository.CompanyRepository;
import com.example.jobmatrix.dto.request.CreateRecruiterProfileRequest;
import com.example.jobmatrix.dto.request.UpdateRecruiterProfileRequest;
import com.example.jobmatrix.dto.response.RecruiterProfileResponse;
import com.example.jobmatrix.exception.BadRequestException;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.mapper.RecruiterProfileMapper;
import com.example.jobmatrix.user.model.RecruiterProfile;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.RecruiterProfileRepository;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RecruiterProfileMapper recruiterProfileMapper;

    public RecruiterProfileResponse createProfile(
            Long userId,
            CreateRecruiterProfileRequest request
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        if(recruiterProfileRepository.existsByUser(user)) {

            throw new BadRequestException(
                    "Recruiter profile already exists"
            );
        }

        Company company =
                companyRepository.findById(
                                request.getCompanyId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Company not found"
                                )
                        );

        RecruiterProfile profile =
                RecruiterProfile.builder()
                        .user(user)
                        .company(company)
                        .department(
                                request.getDepartment()
                        )
                        .verified(false)
                        .build();

        recruiterProfileRepository.save(profile);

        return recruiterProfileMapper.toResponse(
                profile
        );
    }

    public RecruiterProfileResponse getProfile(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        RecruiterProfile profile =
                recruiterProfileRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Recruiter profile not found"
                                )
                        );

        return recruiterProfileMapper.toResponse(
                profile
        );
    }

    public RecruiterProfileResponse updateProfile(
            Long userId,
            UpdateRecruiterProfileRequest request
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        RecruiterProfile profile =
                recruiterProfileRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Recruiter profile not found"
                                )
                        );

        if(request.getDepartment() != null) {

            profile.setDepartment(
                    request.getDepartment()
            );
        }

        if(request.getCompanyId() != null) {

            Company company =
                    companyRepository.findById(
                                    request.getCompanyId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Company not found"
                                    )
                            );

            profile.setCompany(company);
        }

        recruiterProfileRepository.save(profile);

        return recruiterProfileMapper.toResponse(
                profile
        );
    }
    public List<RecruiterProfileResponse>
    getRecruitersByCompany(
            Long companyId
    ) {

        Company company =
                companyRepository.findById(
                        companyId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found"
                        )
                );

        return recruiterProfileRepository
                .findByCompany(company)
                .stream()
                .map(recruiterProfileMapper::toResponse)
                .toList();
    }
}