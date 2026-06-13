package com.example.jobmatrix.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    private String email;

    private String otp;

    private String newPassword;
}