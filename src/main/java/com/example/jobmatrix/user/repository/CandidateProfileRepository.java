package com.example.jobmatrix.user.repository;

import com.example.jobmatrix.user.model.CandidateProfile;
import com.example.jobmatrix.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateProfileRepository
        extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByUser(
            User user
    );

    boolean existsByUser(
            User user
    );

    @EntityGraph(attributePaths = "skills")
    Optional<CandidateProfile> findByUserId(Long userId);

}