package com.bsit.uniread.application.services.otp;

import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.auth.Otp;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.OtpRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserService userService;

    /**
     * Get the Otp by id
     * @params uuid
     * @return Otp
     */
    public Otp getOtpById(UUID uuid) {
        return otpRepository.findById(uuid)
                .orElseThrow( () -> new ResourceNotFoundException("Unable to verify otp"));
    }


    /**
     * Generate a new otp for the email
     * @params email
     * @return Otp
     */
    final public Otp generateOtp(final String email) {
        final User user = userService.getUserByEmailOrThrow(email);
        return otpRepository.save(
            Otp.builder()
                .email(user.getEmail())
                .createdAt(DateUtil.now())
                .updatedAt(DateUtil.now())
                .build()
        );
    }
}
