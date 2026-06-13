package com.example.jobmatrix.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterInvitationResponse {

    private Long id;

    private String email;

    private String companyName;

    private String token;

    private boolean accepted;

    private LocalDateTime expiresAt;
}