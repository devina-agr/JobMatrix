package com.example.jobmatrix.company.model;

import com.example.jobmatrix.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "companies",
        indexes = {

                @Index(
                        name = "idx_company_name",
                        columnList = "name"
                ),

                @Index(
                        name = "idx_company_industry",
                        columnList = "industry"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(
            nullable = false,
            unique = true
    )
    private String name;

    @Enumerated(EnumType.STRING)
    private Industry industry;

    private String websiteUrl;

    private String logoUrl;

    private String logoPublicId;

    @Column(length = 3000)
    private String description;

    private String headquarters;

    private Integer employeeCount;

    @Column(nullable = false)
    private boolean verified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "manager_id",
            nullable = false
    )
    private User manager;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}