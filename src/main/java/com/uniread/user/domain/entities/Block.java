package com.uniread.user.domain.entities;

import com.uniread.auth.domain.entities.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "blocks",
        indexes = {
                @Index(name = "idx_blocks_blocker_id", columnList = "blocker_id"),
                @Index(name = "idx_blocks_blocked_id", columnList = "blocked_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_blocker_blocked", columnNames = {"blocker_id", "blocked_id"})
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id", nullable = false)
    private User blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id", nullable = false)
    private User blocked;

    @CreationTimestamp
    private Instant createdAt;

}