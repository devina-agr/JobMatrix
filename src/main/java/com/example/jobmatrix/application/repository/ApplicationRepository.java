package com.example.jobmatrix.application.repository;

import com.example.jobmatrix.application.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
