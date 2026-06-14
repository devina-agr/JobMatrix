package com.example.jobmatrix.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Set;


@Entity
@Table(
         name = "candidate_profiles",
         indexes = {
                 @Index(name = "idx_candidate_location", columnList = "location"),
                 @Index(name = "idx_candidate_experience",columnList = "experience")
         }

      )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateProfile{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true,nullable = false)
    private User user;
    @NotBlank
    @Column(nullable = false)
    private String location;
    @NotNull
    @Column(nullable = false)
    private Integer experience;
    @ElementCollection
    @CollectionTable(name = "candidate_skills",
            joinColumns = @JoinColumn(name = "candidate_profile_id"),
            indexes = {
                 @Index(
                         name = "idx_candidate_skill",
                         columnList = "skill"
                 )
            }
    )
    @Column(name = "skill",nullable = false)
    private Set<String> skills;
    @Column(nullable = false)
    @NotBlank
    private String resumeUrl;
    @NotBlank
    @Column(nullable = false)
    private String resumePublicId;
    @Column(length = 2000)
    private String bio;
    private String githubUrl;
    private String linkedinUrl;
}
