package com.example.jobmatrix.user.service;

import com.example.jobmatrix.dto.response.UserResponse;
import com.example.jobmatrix.exception.BadRequestException;
import com.example.jobmatrix.exception.ResourceNotFoundException;
import com.example.jobmatrix.user.model.Role;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUser(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return mapToResponse(user);
    }

    public List<UserResponse> getAllUsers() {

        return userRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void enableUser(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );
        if(user.isEnabled()){
            return;
        }
        user.setEnabled(true);

        userRepository.save(user);
    }

    public void disableUser(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );
        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new BadRequestException(
                    "Admin accounts cannot be disabled"
            );
        }
        if(!user.isEnabled()){
            return;
        }
        user.setEnabled(false);
        user.setTokenVersion(
                user.getTokenVersion() + 1
        );
        userRepository.save(user);
    }

    public void deactivateUser(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new BadRequestException(
                    "Admin accounts cannot be disabled"
            );
        }

        user.setEnabled(false);
        user.setTokenVersion(
                user.getTokenVersion() + 1
        );
        userRepository.save(user);
    }

    public List<UserResponse> getUsersByRole(
            Role role
    ) {
        return userRepository
                .findByRoleOrderByCreatedAtDesc(role)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private UserResponse mapToResponse(
            User user
    ) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
