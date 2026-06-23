package com.example.jobmatrix.user.controller;

import com.example.jobmatrix.dto.response.UserResponse;
import com.example.jobmatrix.security.UserPrincipal;
import com.example.jobmatrix.user.model.Role;
import com.example.jobmatrix.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ResponseEntity.ok(
                userService.getUser(
                        principal.getId()
                )
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                userService.getUser(
                        userId
                )
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/enable")
    public ResponseEntity<String> enableUser(
            @PathVariable Long userId
    ) {

        userService.enableUser(
                userId
        );

        return ResponseEntity.ok(
                "User enabled successfully"
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/disable")
    public ResponseEntity<String> disableUser(
            @PathVariable Long userId
    ) {

        userService.disableUser(
                userId
        );

        return ResponseEntity.ok(
                "User disabled successfully"
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId
    ) {

        userService.deactivateUser(
                userId
        );

        return ResponseEntity.noContent()
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> getUsersByRole(
            @PathVariable Role role
    ) {

        return ResponseEntity.ok(
                userService.getUsersByRole(role)
        );
    }
}