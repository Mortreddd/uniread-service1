package com.uniread.auth.repositories;

import com.uniread.auth.domain.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID> {
}
