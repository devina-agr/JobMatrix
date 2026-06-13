package com.example.jobmatrix.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecruiterInvitationRequest {

    @Email
    @NotBlank
    private String email;
}