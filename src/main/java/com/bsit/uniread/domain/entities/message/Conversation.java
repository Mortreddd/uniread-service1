package com.bsit.uniread.domain.entities.message;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "conversations",
    indexes = {
        @Index(name = "idx_conversations_conversation_name", columnList = "name"),
        @Index(name = "idx_conversations_conversation_last_message_id", columnList = "last_message_id")
    })
@Getter
@Setter
@Builder
@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String avatarPhoto;
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<Participant> participants = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<Message> messages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Message lastMessage;

    @CreationTimestamp
    private Instant createdAt;

    private Boolean isGroup;
}