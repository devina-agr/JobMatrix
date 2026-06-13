package com.example.jobmatrix.otp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequest {

    private String email;

    private String otp;
}