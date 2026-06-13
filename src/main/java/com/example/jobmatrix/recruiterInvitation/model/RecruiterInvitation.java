package com.example.jobmatrix.recruiterInvitation.model;

import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recruiter_invitations")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RecruiterInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String token;

    private boolean accepted;

    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    private User invitedBy;

}