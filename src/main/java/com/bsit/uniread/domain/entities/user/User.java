package com.bsit.uniread.domain.entities.user;

import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookComment;
import com.bsit.uniread.domain.entities.book.BookCommentLike;
import com.bsit.uniread.infrastructure.security.validations.constraints.UniqueEmail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "users", indexes = {
        @Index(name = "idx_first_name", columnList = "first_name"),
        @Index(name = "idx_last_name", columnList = "last_name"),
        @Index(name = "idx_username", columnList = "username")
})
@Builder
@Data
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String facebookUuid;
    private String googleUuid;

    private String firstName;
    private String lastName;

    private String username;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String photoUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonManagedReference
    private Role role;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime emailVerifiedAt;

    private String fcmToken;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime bannedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

    @Transient
    private Boolean isEmailVerified;
    @Transient
    private Boolean isUser;
    @Transient
    private Boolean isSuperAdmin;
    @Transient
    private Boolean isAdmin;
    @Transient
    private Boolean isBanned;
    @Transient
    private Long followersCount;
    @Transient
    private Long followingsCount;
    @Transient
    private Long storiesCount;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Book> books;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_comment_id")
    @JsonManagedReference
    private BookComment bookComment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_comment_like_id")
    @JsonManagedReference
    private BookCommentLike bookCommentLike;

    @OneToMany(targetEntity = UserArchive.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_archive_id")
    @JsonManagedReference
    private List<UserArchive> userArchive;

    @OneToMany(mappedBy = "following", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Follow> followers;

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Follow> followings;

    public Long getFollowersCount() {
        return (long) followers.size();
    }

    public Long getFollowingsCount() {
        return (long) followings.size();
    }

    public Long getStoriesCount() {
        return (long) books.size();
    }

    public Boolean getIsEmailVerified() {
        return emailVerifiedAt != null;
    }

    public Boolean getIsUser() {
        return role.getName() == RoleName.USER;
    }

    public Boolean getIsSuperAdmin() {
        return role.getName() == RoleName.SUPER_ADMIN;
    }

    public Boolean getIsAdmin() {
        return role.getName() == RoleName.ADMIN;
    }

    public Boolean getIsBanned() {
        return bannedAt != null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName().name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
