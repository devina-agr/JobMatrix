package com.example.jobmatrix.dto.request;

import com.example.jobmatrix.company.model.Industry;
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
    
    private Industry industry;

    private String websiteUrl;

    private String description;

    private String headquarters;

    private Integer employeeCount;
}