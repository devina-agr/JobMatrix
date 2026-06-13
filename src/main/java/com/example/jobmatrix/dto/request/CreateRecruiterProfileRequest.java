package com.example.jobmatrix.dto.request;

import com.example.jobmatrix.user.model.Department;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecruiterProfileRequest {

    private Long companyId;

    private Department department;
}