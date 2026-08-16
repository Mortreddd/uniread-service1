package com.uniread.user.domain.entities;

import com.uniread.auth.domain.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_socials", indexes = {
        @Index(name = "idx_user_socials_user_id", columnList = "user_id")
})
@Entity(name = "UserSocial")
public class UserSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String personalLink;
    private String discordLink;
}
