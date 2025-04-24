package com.bsit.uniread.domain.entities;

import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "follows", indexes = {
        @Index(name = "idx_following_id", columnList = "following_id"),
        @Index(name = "idx_follower_id", columnList = "follower_id")
})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Transient
    private Boolean isMutualFollow;

    public Boolean getIsMutualFollow() {
        return follower.getFollowings().contains(this);
    }
}
