package com.example.jobmatrix.dto.response;

import com.example.jobmatrix.notification.model.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    private NotificationType type;

    private String message;

    private boolean read;

    private LocalDateTime createdAt;
}