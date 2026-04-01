package com.bsit.uniread.infrastructure.repositories;

import com.bsit.uniread.domain.entities.auth.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID>, CrudRepository<Otp, UUID> {
}
