package com.uniread.auth.service;

import com.uniread.auth.domain.entities.VerificationToken;
import com.uniread.auth.repositories.VerificationTokenRepository;
import com.uniread.common.exceptions.ValidationException;
import com.uniread.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository repository;

    public VerificationToken getVerificationToken(String token) {
        return repository.findById(token)
                .orElseThrow(() -> new ValidationException("Invalid token"));

    }

    @Transactional
    public void markUsed(VerificationToken vt) {
        vt.markAsUsed();
        repository.save(vt);
    }

    public VerificationToken generateVerificationToken(UUID userId, String email) {
        var vt = VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .used(false)
                .email(email)
                .expiryDate(getExpiryTime())
                .userId(userId)
                .build();
        return repository.save(vt);
    }

    public void validateToken(VerificationToken vt) {

        if (vt.isUsed()) {
            throw new ValidationException("Token already used");
        }

        if (vt.isExpired()) {
            throw new ValidationException("Token expired");
        }

    }

    private Instant getExpiryTime() {
        return DateUtil.now().plus(15, ChronoUnit.MINUTES);
    }

}
