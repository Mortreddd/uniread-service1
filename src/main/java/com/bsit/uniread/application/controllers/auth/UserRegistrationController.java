package com.bsit.uniread.application.controllers.auth;


import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.auth.SuccessRegistrationResponse;
import com.bsit.uniread.application.services.auth.AuthService;
import com.bsit.uniread.application.dto.request.auth.UserRegistrationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.AUTH)
public class UserRegistrationController {

    private final AuthService authService;

    @PostMapping(path = "/register")
    public ResponseEntity<SuccessRegistrationResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest registrationRequest
    ) {
        authService.registerUser(registrationRequest);
        SuccessRegistrationResponse response = SuccessRegistrationResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Email verification has been sent")
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(path = "/verify-email/{otpId}")
    public ResponseEntity<String> verifyEmail(
            @PathVariable(name = "otpId") UUID otpId
    ) {
        authService.verifyEmailByOtpId(otpId);
        return ResponseEntity.ok().build();
    }
}
