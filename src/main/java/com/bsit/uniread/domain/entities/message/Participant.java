package com.bsit.uniread.domain.entities.message;

import com.bsit.uniread.domain.entities.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "participants", indexes = {
        @Index(name = "idx_participants_conversation_id", columnList = "conversation_id"),
        @Index(name = "idx_participants_user_id", columnList = "user_id"),
        @Index(name = "idx_participants_conversation_user", columnList = "conversation_id, user_id", unique = true),
        @Index(name = "idx_participants_user_archived", columnList = "user_id, archived"),
        @Index(name = "idx_participants_user_muted", columnList = "user_id, muted")
})
@Getter
@Setter
@Builder
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ParticipantRole role = ParticipantRole.MEMBER;

    private String nickname;
    @Builder.Default
    private Long unreadCount = 0L;
    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean archived = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean muted = false;

    @Column(name = "muted_until")
    private Instant mutedUntil;

    @Column(name = "joined_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant joinedAt;

    @Column(name = "left_at")
    private Instant leftAt;

    @CreationTimestamp
    private Instant createdAt;

    @Version
    private Long version;
}