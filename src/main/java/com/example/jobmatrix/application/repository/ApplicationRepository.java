package com.example.jobmatrix.application.repository;

import com.example.jobmatrix.application.model.Application;
import com.example.jobmatrix.job.model.Job;
import com.example.jobmatrix.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByCandidate(
            User candidate
    );

    List<Application> findByJob(
            Job job
    );

    Optional<Application> findByCandidateAndJob(
            User candidate,
            Job job
    );

    boolean existsByCandidateAndJob(
            User candidate,
            Job job
    );
}