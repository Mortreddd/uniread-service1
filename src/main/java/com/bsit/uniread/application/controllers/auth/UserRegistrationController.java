package com.bsit.uniread.application.controllers.auth;


import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.services.auth.AuthService;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.auth.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.AUTH)
public class UserRegistrationController {

    private final AuthService authService;

    @PostMapping(path = "/register")
    public ResponseEntity<SuccessResponse<String>> registerUser(
            @Validated @RequestBody UserRegistrationRequest registrationRequest
    ) {
        authService.registerUser(registrationRequest);
        final String message = "Email verification has been sent";
        return ResponseEntity.ok()
                .body(SuccessResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .message(message)
                        .build()
                );
    }

    @GetMapping(path = "/verify-email/{otpId}")
    public ResponseEntity<SuccessResponse<String>> verifyEmail(
            @PathVariable(name = "otpId") UUID otpId
    ) {
        authService.verifyEmailByOtpId(otpId);
        final String message = "Successfully verified email";
        return ResponseEntity.ok()
                .body(SuccessResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .message(message)
                        .build()
                );
    }
}
