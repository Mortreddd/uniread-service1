package com.bsit.uniread.infrastructure.repositories.user;

import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository
    extends JpaRepository<Role, Integer>, CrudRepository<Role, Integer> {

    Optional<Role> findByName(RoleName roleName);
}
