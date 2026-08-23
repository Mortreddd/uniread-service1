package com.uniread.auth.domain.entities;

import com.uniread.user.domain.entities.UserProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.*;

@Table(name = "users", indexes = {
        @Index(name = "idx_users_username", columnList = "username"),
        @Index(name = "idx_users_email", columnList = "email"),
    }, uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private Instant emailVerifiedAt;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant bannedAt;
    private Instant unbannedAt;

    private Instant deletedAt;

    @OneToOne(mappedBy = "user")
    private UserProfile profile;

    public Boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"),
        indexes = { @Index(name = "idx_user_roles_user_id_role_id", columnList = "user_id, role_id") }
    )
    private Set<Role> roles = new HashSet<>();

}
