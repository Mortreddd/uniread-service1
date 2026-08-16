package com.uniread.user.repositories;

import com.uniread.auth.domain.entities.User;
import com.uniread.user.domain.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID>{

    Optional<UserProfile> findByUserId(UUID userId);

    UUID user(User user);
}
