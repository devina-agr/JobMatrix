package com.example.jobmatrix.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRecruiterProfileRequest {

    private Long companyId;

    private String department;
}