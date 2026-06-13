package com.example.jobmatrix.auth.model;

import com.example.jobmatrix.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "refresh_tokens",
        indexes = {

                @Index(
                        name = "idx_refresh_token",
                        columnList = "token"
                ),

                @Index(
                        name = "idx_refresh_user",
                        columnList = "user_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 512
    )
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        if (this.revoked == false) {
            this.revoked = false;
        }
    }

    public boolean isExpired() {
        return expiresAt.isBefore(
                LocalDateTime.now()
        );
    }

    public boolean isValid() {
        return !revoked && !isExpired();
    }
}