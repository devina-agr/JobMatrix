package com.example.jobmatrix.user.repository;

import com.example.jobmatrix.user.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(@Email String email);

    Optional<User> findByUsername(String username);

    List<User> findAllByOrderByCreatedAtDesc();
}
