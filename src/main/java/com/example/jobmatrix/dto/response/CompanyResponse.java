package com.example.jobmatrix.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {

    private Long id;

    private String name;

    private String industry;

    private String websiteUrl;

    private boolean verified;
}