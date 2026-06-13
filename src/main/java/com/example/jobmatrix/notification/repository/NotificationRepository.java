package com.example.jobmatrix.notification.repository;

import com.example.jobmatrix.notification.model.Notification;
import com.example.jobmatrix.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(
            User user
    );

    List<Notification> findByUserAndReadFalseOrderByCreatedAtDesc(
            User user
    );

    long countByUserAndReadFalse(
            User user
    );
}