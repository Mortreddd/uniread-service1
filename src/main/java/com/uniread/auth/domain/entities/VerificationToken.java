package com.uniread.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Table(name = "verification_tokens", indexes = {
        @Index(name = "idx_verification_tokens_user_id", columnList = "user_id"),
        @Index(name = "idx_verification_tokens_email", columnList = "email"),
        @Index(name = "idx_verification_tokens_token", columnList = "token"),
        @Index(name = "idx_verification_tokens_expiry_date", columnList = "expiry_date")
})
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {

    @Id
    @Column(name = "token", nullable = false, length = 500)
    private String token;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "otp", length = 10)
    private String otp;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(name = "used")
    @Builder.Default
    private boolean used = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Helper methods
    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    public boolean isValid() {
        return !isExpired() && !used;
    }

    public void markAsUsed() {
        this.used = true;
    }
}