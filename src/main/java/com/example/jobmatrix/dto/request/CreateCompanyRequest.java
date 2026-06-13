package com.example.jobmatrix.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCompanyRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String industry;

    private String websiteUrl;

    private String logoUrl;

    private String description;

    private String headquarters;

    private Integer employeeCount;
}