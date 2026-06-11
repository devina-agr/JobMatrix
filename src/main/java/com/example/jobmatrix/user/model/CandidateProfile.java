package com.example.jobmatrix.user.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "candidate_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateProfile {

    @Id
    private String id;
    private String userId;
    @Indexed
    private String location;
    private Integer experience;
    @TextIndexed
    private List<String> skills;
    private String resumeUrl;
    @TextIndexed
    private String bio;
    private String githubUrl;
    private String linkedinUrl;
}
