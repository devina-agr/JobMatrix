package com.example.jobmatrix.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCandidateProfileRequest {

    @NotBlank
    private String location;

    @NotNull
    private Integer experience;

    private Set<String> skills;

    @NotBlank
    private String resumeUrl;

    private String bio;

    private String githubUrl;

    private String linkedinUrl;
}