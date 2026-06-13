package com.example.jobmatrix.user.model;

import com.example.jobmatrix.company.model.Company;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(
          name = "recruiter_profiles",
        indexes = {
                @Index(
                        name = "idx_recruiter_department",
                        columnList = "department"
                ),
                @Index(
                        name = "idx_recruiter_verified",
                        columnList = "verified"
                )
        }
      )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",unique = true)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;
    @Column(nullable = false)
    private boolean verified;
}
