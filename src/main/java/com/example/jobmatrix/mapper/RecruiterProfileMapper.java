package com.example.jobmatrix.mapper;

import com.example.jobmatrix.dto.response.RecruiterProfileResponse;
import com.example.jobmatrix.user.model.RecruiterProfile;
import org.springframework.stereotype.Component;

@Component
public class RecruiterProfileMapper {

    public RecruiterProfileResponse toResponse(
            RecruiterProfile profile
    ) {

        return RecruiterProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .username(profile.getUser().getUsername())
                .email(profile.getUser().getEmail())
                .companyId(
                        profile.getCompany() != null
                                ? profile.getCompany().getId()
                                : null
                )
                .companyName(
                        profile.getCompany() != null
                                ? profile.getCompany().getName()
                                : null
                )
                .department(profile.getDepartment().name())
                .verified(profile.isVerified())
                .build();
    }
}