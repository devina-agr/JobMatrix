package com.example.jobmatrix.user.repository;

import com.example.jobmatrix.user.model.RecruiterProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterProfileRepository extends MongoRepository<RecruiterProfile,String> {
}
