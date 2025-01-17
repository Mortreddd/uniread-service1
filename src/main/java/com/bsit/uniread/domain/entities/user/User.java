package com.bsit.uniread.domain.entities.user;

import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookComment;
import com.bsit.uniread.domain.entities.book.BookCommentLike;
import com.bsit.uniread.domain.entities.message.Message;
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
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "users")
@Builder
@Data
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String googleUuid;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @UniqueEmail
    private String email;

    @JsonIgnore
    private String password;

    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonBackReference
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<Book> books;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_comment_id")
    @JsonManagedReference
    private BookComment bookComment;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_comment_like_id")
    @JsonManagedReference
    private BookCommentLike bookCommentLike;

    @OneToMany(targetEntity = UserArchive.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_archive_id")
    @JsonManagedReference
    private List<UserArchive> userArchive;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Follow> followers;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Follow> followings;

    public Boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    public Boolean isSuperAdmin() {
        return role.getName() == RoleName.SUPER_ADMIN;
    }

    public Boolean isAdmin() {
        return role.getName() == RoleName.ADMIN;
    }

    public Boolean isBanned() {
        return bannedAt != null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName().name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
