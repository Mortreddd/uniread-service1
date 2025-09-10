package com.bsit.uniread.domain.entities.user;

import com.bsit.uniread.domain.entities.book.Bookmark;
import com.bsit.uniread.domain.entities.collaborator.Collaborator;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.notification.Notification;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookComment;
import com.bsit.uniread.domain.entities.book.BookCommentLike;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "users", indexes = {
        @Index(name = "idx_first_name", columnList = "first_name"),
        @Index(name = "idx_last_name", columnList = "last_name"),
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime emailVerifiedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime bannedAt;
    private LocalDateTime unbannedAt;

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
    @Transient
    private Long publishedStoriesCount;
    @Transient
    private Long draftedStoriesCount;
    @Transient
    private String fullName;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Book> books = new ArrayList<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private Collaborator collaborator;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_comment_id")
    @JsonBackReference
    private BookComment bookComment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_comment_like_id")
    @JsonBackReference
    private BookCommentLike bookCommentLike;

    @Builder.Default
    @OneToMany(mappedBy = "following", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Follow> followers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Follow> followings = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Notification> notifications = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Bookmark> bookmarks = new ArrayList<>();

    public Long getFollowersCount() {
        return (long) followers.size();
    }

    public Long getFollowingsCount() {
        return (long) followings.size();
    }

    public Long getStoriesCount() {
        return (long) books.size();
    }

    public Long getPublishedStoriesCount() {
        return (long) books
                .stream()
                .filter(Book::getIsPublished)
                .count();
    }

    public Long getDraftedStoriesCount() {
        return (long) books
                .stream()
                .filter((b) -> !b.getIsPublished())
                .count();
    }

    public Boolean getIsEmailVerified() {
        return emailVerifiedAt != null;
    }

    public Boolean getIsUser() {
        return role == Role.USER;
    }

    public Boolean getIsSuperAdmin() {
        return role == Role.SUPER_ADMIN;
    }

    public Boolean getIsAdmin() {
        return role == Role.ADMIN;
    }

    public Boolean getIsBanned() {
        return bannedAt != null;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.bannedAt == null;
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
        return this.deletedAt == null && emailVerifiedAt != null;
    }
}
