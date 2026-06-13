package com.example.jobmatrix.company.service;

import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.company.repository.CompanyRepository;
import com.example.jobmatrix.dto.request.CreateCompanyRequest;
import com.example.jobmatrix.dto.request.UpdateCompanyRequest;
import com.example.jobmatrix.dto.response.CompanyResponse;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.mapper.CompanyMapper;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    private final CompanyMapper companyMapper;

    public CompanyResponse createCompany(
            CreateCompanyRequest request,
            Long ownerId
    ) {

        User owner =
                userRepository.findById(ownerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Owner not found"
                                )
                        );

        Company company =
                Company.builder()
                        .name(request.getName())
                        .industry(request.getIndustry())
                        .websiteUrl(
                                request.getWebsiteUrl()
                        )
                        .logoUrl(
                                request.getLogoUrl()
                        )
                        .description(
                                request.getDescription()
                        )
                        .headquarters(
                                request.getHeadquarters()
                        )
                        .employeeCount(
                                request.getEmployeeCount()
                        )
                        .verified(false)
                        .manager(owner)
                        .build();

        companyRepository.save(company);

        return companyMapper.toResponse(
                company
        );
    }

    public CompanyResponse getCompany(
            Long companyId
    ) {

        Company company =
                companyRepository.findById(
                                companyId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Company not found"
                                )
                        );

        return companyMapper.toResponse(
                company
        );
    }

    public Page<CompanyResponse> getAllCompanies(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("name")
                );

        return companyRepository
                .findAll(pageable)
                .map(
                        companyMapper::toResponse
                );
    }

    public CompanyResponse updateCompany(
            Long companyId,
            UpdateCompanyRequest request
    ) {

        Company company =
                companyRepository.findById(
                                companyId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Company not found"
                                )
                        );

        if(request.getName()!=null)
            company.setName(
                    request.getName()
            );

        if(request.getIndustry()!=null)
            company.setIndustry(
                    request.getIndustry()
            );

        if(request.getWebsiteUrl()!=null)
            company.setWebsiteUrl(
                    request.getWebsiteUrl()
            );

        if(request.getLogoUrl()!=null)
            company.setLogoUrl(
                    request.getLogoUrl()
            );

        if(request.getDescription()!=null)
            company.setDescription(
                    request.getDescription()
            );

        if(request.getHeadquarters()!=null)
            company.setHeadquarters(
                    request.getHeadquarters()
            );

        if(request.getEmployeeCount()!=null)
            company.setEmployeeCount(
                    request.getEmployeeCount()
            );

        companyRepository.save(company);

        return companyMapper.toResponse(
                company
        );
    }

    public void deleteCompany(
            Long companyId
    ) {

        Company company =
                companyRepository.findById(
                                companyId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Company not found"
                                )
                        );

        companyRepository.delete(
                company
        );
    }
}