package com.bsit.uniread.application.controllers.auth;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.auth.ForgotPasswordRequest;
import com.bsit.uniread.application.dto.request.auth.LoginRequest;
import com.bsit.uniread.application.dto.response.auth.LoginResponse;
import com.bsit.uniread.application.services.auth.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = ApiEndpoints.AUTH)
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticate user and accepts a LoginRequest object for logging in
     * @param loginRequest
     * @return LoginResponse
     */
    @PostMapping(path = "/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> verifyLogin(
            @Validated @RequestBody LoginRequest loginRequest
    ) {

        String message = "Successfully logged in";
        LoginResponse response = authService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok()
                .body(SuccessResponse.<LoginResponse>builder()
                        .data(response)
                        .code(HttpStatus.OK.value())
                        .message(message)
                        .build()
                );
    }

    /**
     * Sends a forgot password based on user's email
     * @param forgotPasswordRequest
     * @return ResponseEntity
     * @throws MessagingException
     */
    @PostMapping(path = "/forgot-password", consumes = {"application/json"})
    public ResponseEntity<SuccessResponse<String>> sendForgotPassword(
            @Validated @RequestBody ForgotPasswordRequest forgotPasswordRequest
    ) throws MessagingException {
        authService.sendForgotPassword(forgotPasswordRequest.getEmail());
        String message = "Email Confirmation has been sent";

        return ResponseEntity.ok()
                .body(SuccessResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .message(message)
                        .build()
                );

    }

    /**
     * Verify the url was sent to the user's email
     * @param id
     * @return ResponseEntity
     */
    @GetMapping(path = "/forgot-password")
    public ResponseEntity<SuccessResponse<String>> verifyForgotPassword(
            @RequestParam("id") UUID id
    ) {
        authService.verifyEmailByOtpId(id);
        String message = "Successfully verified the email";
        return ResponseEntity.ok()
                .body(
                        SuccessResponse.<String>builder()
                                .code(HttpStatus.OK.value())
                                .message(message)
                                .build()
                );
    }
}
