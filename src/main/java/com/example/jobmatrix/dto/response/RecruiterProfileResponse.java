package com.example.jobmatrix.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfileResponse {

    private Long id;

    private Long userId;

    private String username;

    private String email;

    private Long companyId;

    private String companyName;

    private String department;

    private boolean verified;
}