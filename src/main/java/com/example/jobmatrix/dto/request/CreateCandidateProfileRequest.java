package com.example.jobmatrix.dto.request;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCandidateProfileRequest {

    private String location;

    private Integer experience;

    private Set<String> skills;

    private String bio;

    private String githubUrl;

    private String linkedinUrl;
}