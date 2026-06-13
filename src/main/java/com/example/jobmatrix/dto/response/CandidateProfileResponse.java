package com.example.jobmatrix.dto.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateProfileResponse {

    private Long id;

    private Long userId;

    private String username;

    private String email;

    private String location;

    private Integer experience;

    private Set<String> skills;

    private String resumeUrl;

    private String bio;

    private String githubUrl;

    private String linkedinUrl;
}