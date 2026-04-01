package com.bsit.uniread.domain.entities.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@Table(name = "user_socials", indexes = {
        @Index(name = "idx_user_socials_user_id", columnList = "user_id")
})
@Entity(name = "UserSocial")
public class UserSocial {

    private UUID id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private User user;

    private String personalLink;
    private String discordLink;
}
