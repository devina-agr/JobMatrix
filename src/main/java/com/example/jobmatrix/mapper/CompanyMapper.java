package com.example.jobmatrix.mapper;

import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.dto.response.CompanyResponse;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public CompanyResponse toResponse(
            Company company
    ) {

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .industry(company.getIndustry())
                .websiteUrl(company.getWebsiteUrl())
                .logoUrl(company.getLogoUrl())
                .description(company.getDescription())
                .headquarters(company.getHeadquarters())
                .employeeCount(company.getEmployeeCount())
                .verified(company.isVerified())
                .build();
    }
}