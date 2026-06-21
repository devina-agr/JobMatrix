package com.example.jobmatrix.company.service;

import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.company.repository.CompanyRepository;
import com.example.jobmatrix.dto.request.CreateCompanyRequest;
import com.example.jobmatrix.dto.request.UpdateCompanyRequest;
import com.example.jobmatrix.dto.response.CloudinaryUploadResponse;
import com.example.jobmatrix.dto.response.CompanyResponse;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.mapper.CompanyMapper;
import com.example.jobmatrix.upload.CloudinaryService;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    private final CompanyMapper companyMapper;

    private final CloudinaryService cloudinaryService;

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
                        .description(
                                request.getDescription()
                        )
                        .headquarters(
                                request.getHeadquarters()
                        )
                        .employeeCount(
                                request.getEmployeeCount()
                        )
                        .logoUrl(null)
                        .logoPublicId(null)
                        .verified(false)
                        .manager(owner)
                        .build();

        companyRepository.save(company);

        return companyMapper.toResponse(
                company
        );
    }

    public CompanyResponse getMyCompany(
            Long managerId
    ) {

        User manager =
                userRepository.findById(managerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Manager not found"
                                )
                        );

        Company company =
                companyRepository.findByManager(manager)
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

    public CompanyResponse updateMyCompany(
            Long managerId,
            UpdateCompanyRequest request
    ) {

        User manager =
                userRepository.findById(managerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Manager not found"
                                )
                        );

        Company company =
                companyRepository.findByManager(manager)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Company not found"
                                )
                        );

        if(request.getName() != null)
            company.setName(request.getName());

        if(request.getIndustry() != null)
            company.setIndustry(request.getIndustry());

        if(request.getWebsiteUrl() != null)
            company.setWebsiteUrl(request.getWebsiteUrl());

        if(request.getDescription() != null)
            company.setDescription(request.getDescription());

        if(request.getHeadquarters() != null)
            company.setHeadquarters(request.getHeadquarters());

        if(request.getEmployeeCount() != null)
            company.setEmployeeCount(request.getEmployeeCount());

        companyRepository.save(company);

        return companyMapper.toResponse(company);
    }
    public void deleteMyCompany(
            Long managerId
    ) {
        User manager =
                userRepository.findById(managerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Manager not found"
                                )
                        );

        Company company =
                companyRepository.findByManager(
                                manager
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

    public String uploadMyCompanyLogo(
            Long managerId,
            MultipartFile file
    ) {

        User manager =
                userRepository.findById(managerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Manager not found"
                                )
                        );

        Company company =
                companyRepository.findByManager(
                                manager
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Company not found"
                                )
                        );

        if(company.getLogoPublicId() != null){

            cloudinaryService.deleteFile(
                    company.getLogoPublicId()
            );
        }

        CloudinaryUploadResponse upload =
                cloudinaryService.uploadCompanyLogo(
                        file
                );

        company.setLogoUrl(
                upload.getUrl()
        );

        company.setLogoPublicId(
                upload.getPublicId()
        );

        companyRepository.save(
                company
        );

        return upload.getUrl();
    }

    public void deleteMyCompanyLogo(
            Long managerId
    ) {

        User manager =
                userRepository.findById(managerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Manager not found"
                                )
                        );

        Company company =
                companyRepository.findByManager(
                                manager
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Company not found"
                                )
                        );

        if(company.getLogoPublicId() != null){

            cloudinaryService.deleteFile(
                    company.getLogoPublicId()
            );

            company.setLogoUrl(null);

            company.setLogoPublicId(null);

            companyRepository.save(
                    company
            );
        }
    }

    public void verifyCompany(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Company not found with id: " + companyId
                        ));

        company.setVerified(true);

        companyRepository.save(company);
    }
}