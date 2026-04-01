package com.bsit.uniread.domain.entities.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "blocks", indexes = {
        @Index(name = "idx_blocks_blocker_id", columnList = "blocker_id"),
        @Index(name = "idx_blocks_blocked_id", columnList = "blocked_id")
})
@Data
@Builder
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "blocker_id")
    @Column(name = "blocker_id", nullable = false)
    private User blocker;

    @ManyToOne
    @JoinColumn(name = "blocked_id")
    @Column(name = "blocked_id", nullable = false)
    private User blocked;

    @CreationTimestamp
    private Instant createdAt;

}
