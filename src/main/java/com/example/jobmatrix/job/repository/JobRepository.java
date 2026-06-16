package com.example.jobmatrix.job.repository;

import com.example.jobmatrix.company.model.Company;
import com.example.jobmatrix.job.model.Job;
import com.example.jobmatrix.job.model.JobType;
import com.example.jobmatrix.user.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface JobRepository
        extends JpaRepository<Job, Long> {

    Page<Job> findByActiveTrue(Pageable pageable);

    Page<Job> findByCompany(
            Company company,
            Pageable pageable
    );

    Page<Job> findByRecruiter(
            User recruiter,
            Pageable pageable
    );

    Page<Job> findByLocationContainingIgnoreCaseAndActiveTrue(
            String location,
            Pageable pageable
    );

    Page<Job> findByJobTypeAndActiveTrue(
            JobType jobType,
            Pageable pageable
    );


    @EntityGraph(attributePaths = {
            "company",
            "skills"
    })

@Query("""
    SELECT DISTINCT j
    FROM Job j
    JOIN j.skills s
    WHERE s IN :skills
    AND j.active = true
            """)
    List<Job> recommendJobs(
            @Param("skills")
            Set<String> skills
    );
}