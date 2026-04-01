package com.bsit.uniread.domain.entities;

import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Table(name = "follows", indexes = {
        @Index(name = "idx_follows_following_id", columnList = "following_id"),
        @Index(name = "idx_follows_follower_id", columnList = "follower_id")
})
@Entity
@Getter
@Setter
@Builder
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "follower_id", nullable = false)
    @JsonManagedReference
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "following_id", nullable = false)
    @JsonManagedReference
    private User following;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}
