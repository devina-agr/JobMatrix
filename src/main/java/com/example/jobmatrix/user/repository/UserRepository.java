package com.example.jobmatrix.user.repository;

import com.example.jobmatrix.user.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(@Email String email);
}
