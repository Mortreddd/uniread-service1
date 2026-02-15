package com.bsit.uniread.domain.entities.message;

import com.bsit.uniread.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "participants", indexes = {
        @Index(name = "idx_participants_conversation_id", columnList = "conversation_id"),
        @Index(name = "idx_participants_user_id", columnList = "user_id")
})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime lastReadAt;

    @Column(nullable = false)
    private Boolean muted = false;
    @Column(nullable = false)
    private Boolean archived = false;
    @CreationTimestamp
    private LocalDateTime addedAt;
}
