package com.example.jobmatrix.company.repository;

import com.example.jobmatrix.company.model.Company;
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
}