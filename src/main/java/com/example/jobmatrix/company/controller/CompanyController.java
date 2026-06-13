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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompany(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                companyService.getCompany(id)
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

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long id,
            @RequestBody UpdateCompanyRequest request
    ) {

        return ResponseEntity.ok(
                companyService.updateCompany(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable Long id
    ) {

        companyService.deleteCompany(id);

        return ResponseEntity.noContent().build();
    }
}