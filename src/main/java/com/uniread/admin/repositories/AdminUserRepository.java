package com.uniread.admin.repositories;

import com.uniread.auth.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminUserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {


    @Override
    @EntityGraph(attributePaths = {"profile"})
    Page<User> findAll(Specification<User> spec, Pageable pageable);
}
