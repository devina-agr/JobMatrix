package com.example.jobmatrix.job.model;

import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "jobs",
        indexes = {
                @Index(
                        name = "idx_job_title",
                        columnList ="title"
                ),
                @Index(
                        name = "idx_job_location",
                        columnList = "location"
                ),

                @Index(
                        name = "idx_job_experience",
                        columnList = "experienceRequired"
                ),

                @Index(
                        name = "idx_job_salary",
                        columnList = "salary"
                ),

                @Index(
                        name = "idx_job_type",
                        columnList = "jobType"
                )

        }

)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "recruiter_id",
            nullable = false
    )
    private User recruiter;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "company_id",
            nullable = false
    )
    private Company company;
    @NotBlank
    @Column(nullable = false)
    private String title;
    @Column(nullable = false,length = 5000)
    @NotBlank
    private String jobDescription;
    @ElementCollection
    @CollectionTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            indexes = {
                    @Index(
                            name = "idx_job_skill",
                            columnList = "skill"
                    )
            }
    )
    @Column(name = "skill",nullable = false)
    private Set<String> skills;
    private String eligibility;
    @NotBlank
    @Column(nullable = false)
    private String location;
    @NotNull
    @Column(nullable = false)
    private Double salary;
    @NotNull
    @Column(nullable = false)
    private Integer experienceRequired;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;
    private boolean active;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }
}