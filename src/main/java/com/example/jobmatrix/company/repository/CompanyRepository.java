package com.example.jobmatrix.company.repository;

import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository
        extends JpaRepository<Company,Long> {

    Optional<Company> findByName(
            String name
    );

    boolean existsByName(
            String name
    );

    boolean existsByNameIgnoreCase(String companyName);

    Optional<Company> findByManager(User manager);

    Page<Company> findByBlockedFalse(Pageable pageable);
}