package com.bsit.uniread.infrastructure.repositories.user;

import com.bsit.uniread.domain.entities.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID>, CrudRepository<UserProfile, UUID> {
}
