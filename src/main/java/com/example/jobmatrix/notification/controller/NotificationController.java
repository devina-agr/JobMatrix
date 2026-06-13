package com.example.jobmatrix.notification.controller;

import com.example.jobmatrix.dto.response.NotificationResponse;
import com.example.jobmatrix.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>>
    getNotifications(
            @AuthenticationPrincipal
            com.example.jobmatrix.security.UserPrincipal principal
    ) {

        return ResponseEntity.ok(
                notificationService.getNotifications(
                        principal.getId()
                )
        );
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>>
    getUnreadNotifications(
            @AuthenticationPrincipal
            com.example.jobmatrix.security.UserPrincipal principal
    ) {

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(
                        principal.getId()
                )
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long>
    unreadCount(
            @AuthenticationPrincipal
            com.example.jobmatrix.security.UserPrincipal principal
    ) {

        return ResponseEntity.ok(
                notificationService.unreadCount(
                        principal.getId()
                )
        );
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String>
    markAsRead(
            @PathVariable Long notificationId
    ) {

        notificationService.markAsRead(
                notificationId
        );

        return ResponseEntity.ok(
                "Notification marked as read"
        );
    }
}