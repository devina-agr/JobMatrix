package com.example.jobmatrix.user.repository;

import com.example.jobmatrix.user.model.CandidateProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateProfileRepository extends MongoRepository<CandidateProfile,String> {

}
