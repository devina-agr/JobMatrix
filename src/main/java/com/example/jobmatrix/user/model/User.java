package com.example.jobmatrix.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
          name = "users",
          indexes = {
                  @Index(name = "idx_user_email",columnList = "email")
          }
       )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean enabled;
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    private int tokenVersion;

    @PrePersist
    public void prePersist(){
        this.createdAt=LocalDateTime.now();
    }

}
