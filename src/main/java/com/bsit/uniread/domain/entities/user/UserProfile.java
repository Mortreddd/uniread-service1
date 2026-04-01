package com.bsit.uniread.domain.entities.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "user_profiles", indexes = {
        @Index(name = "idx_user_profiles_user_id", columnList = "user_id"),
        @Index(name = "idx_user_profiles_first_name", columnList = "first_name"),
        @Index(name = "idx_user_profiles_last_name", columnList = "last_name"),
        @Index(name = "idx_user_profiles_display_name", columnList = "display_name")
})
@Entity(name = "UserProfile")
public class UserProfile {

    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private User user;
    private String firstName;
    private String lastName;
    private String displayName;
    private String bio;
    private String coverPhoto;
    private String avatarPhoto;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}
