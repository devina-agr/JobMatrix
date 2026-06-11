package com.example.jobmatrix.job.repository;

import com.example.jobmatrix.job.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobRepository extends MongoRepository<Job,String> {
    List<Job> findByLocation(String location);

    List<Job> findByCompanyName(String companyName);

    List<Job> findBySkillsContaining(String skill);

    List<Job> findByActiveTrue();
}
