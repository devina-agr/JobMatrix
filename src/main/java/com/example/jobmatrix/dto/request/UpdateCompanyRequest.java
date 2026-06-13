package com.example.jobmatrix.dto.request;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCompanyRequest {

    private String name;

    private String industry;

    private String websiteUrl;

    private String logoUrl;

    private String description;

    private String headquarters;

    private Integer employeeCount;
}
