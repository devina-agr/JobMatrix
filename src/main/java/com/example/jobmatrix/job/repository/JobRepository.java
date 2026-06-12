package com.example.jobmatrix.job.repository;

import com.example.jobmatrix.job.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface JobRepository extends JpaRepository<Job,Long> {
    List<Job> findByLocation(String location);

    List<Job> findByCompanyName(String companyName);

    List<Job> findBySkillsContaining(String skill);

    List<Job> findByActiveTrue();

    List<Job> findByTitleContainingIgnoreCase(String keyword);
}
