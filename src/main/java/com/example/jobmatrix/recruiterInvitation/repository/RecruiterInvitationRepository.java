package com.example.jobmatrix.recruiterInvitation.repository;

import com.example.jobmatrix.recruiterInvitation.model.RecruiterInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecruiterInvitationRepository
        extends JpaRepository<RecruiterInvitation, Long> {

    Optional<RecruiterInvitation> findByToken(
            String token
    );

    boolean existsByEmailAndCompanyId(
            String email,
            Long companyId
    );

    List<RecruiterInvitation> findByCompanyId(
            Long companyId
    );

    void deleteByExpiresAtBefore(
            LocalDateTime time
    );
}