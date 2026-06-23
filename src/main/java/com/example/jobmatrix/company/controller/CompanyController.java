package com.example.jobmatrix.company.controller;

import com.example.jobmatrix.company.service.CompanyService;
import com.example.jobmatrix.dto.request.CreateCompanyRequest;
import com.example.jobmatrix.dto.request.UpdateCompanyRequest;
import com.example.jobmatrix.dto.response.CompanyResponse;
import com.example.jobmatrix.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_MANAGER')")
    public ResponseEntity<CompanyResponse> createCompany(
            @RequestBody @Valid CreateCompanyRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        companyService.createCompany(
                                request,
                                principal.getId()
                        )
                );
    }

    @GetMapping("/my-company")
    @PreAuthorize(
            "hasAnyRole('COMPANY_MANAGER','RECRUITER')"
    )
    public ResponseEntity<CompanyResponse> getMyCompany(
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ResponseEntity.ok(
                companyService.getMyCompany(
                        principal.getId()
                )
        );
    }



    @GetMapping
    public ResponseEntity<Page<CompanyResponse>> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                companyService.getAllCompanies(
                        page,
                        size
                )
        );
    }

    @PutMapping("/my-company")
    public ResponseEntity<CompanyResponse> updateCompany(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UpdateCompanyRequest request
    ) {

        return ResponseEntity.ok(
                companyService.updateMyCompany(
                        userPrincipal.getId(),
                        request
                )
        );
    }

    @PutMapping("/my-company/block")
    public ResponseEntity<Void> blockMyCompany(
           @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        companyService.blockMyCompany(userPrincipal.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/my-company/logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> uploadLogo(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("file") MultipartFile file
    ) {

        return ResponseEntity.ok(
                companyService.uploadMyCompanyLogo(
                        userPrincipal.getId(),
                        file
                )
        );
    }

    @DeleteMapping("/my-company/logo")
    public ResponseEntity<Void> deleteLogo(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        companyService.deleteMyCompanyLogo(
                userPrincipal.getId()
        );

        return ResponseEntity.noContent()
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{companyId}/verify")
    public ResponseEntity<String> verifyCompany(
            @PathVariable Long companyId
    ) {

        companyService.verifyCompany(companyId);

        return ResponseEntity.ok(
                "Company verified successfully"
        );
    }

    @PutMapping("/{companyId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> blockCompany(
            @PathVariable Long companyId
    ) {
        companyService.blockCompany(companyId);
        return ResponseEntity.noContent().build();
    }
}