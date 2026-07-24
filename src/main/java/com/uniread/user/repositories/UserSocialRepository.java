package com.uniread.user.repositories;

import com.uniread.user.domain.entities.UserSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserSocialRepository extends JpaRepository<UserSocial, UUID>, CrudRepository<UserSocial, UUID> {
}
