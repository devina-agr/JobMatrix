package com.example.jobmatrix.user.repository;

import com.example.jobmatrix.user.model.RecruiterProfile;
import com.example.jobmatrix.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterProfileRepository
        extends JpaRepository<RecruiterProfile, Long> {

    Optional<RecruiterProfile> findByUser(
            User user
    );

    boolean existsByUser(
            User user
    );
}