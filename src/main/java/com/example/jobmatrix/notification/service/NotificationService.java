package com.example.jobmatrix.notification.service;

import com.example.jobmatrix.dto.response.NotificationResponse;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.notification.model.Notification;
import com.example.jobmatrix.notification.model.NotificationType;
import com.example.jobmatrix.notification.repository.NotificationRepository;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void createNotification(
            User user,
            NotificationType type,
            String message
    ) {

        Notification notification =
                Notification.builder()
                        .user(user)
                        .type(type)
                        .message(message)
                        .build();

        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getNotifications(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<NotificationResponse> getUnreadNotifications(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return notificationRepository
                .findByUserAndReadFalseOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void markAsRead(
            Long notificationId
    ) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                )
                        );

        notification.setRead(true);

        notificationRepository.save(notification);
    }

    public long unreadCount(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return notificationRepository
                .countByUserAndReadFalse(user);
    }

    private NotificationResponse mapToResponse(
            Notification notification
    ) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}