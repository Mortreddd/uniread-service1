package com.uniread.auth.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private Boolean success;
    private String message;
    private UserInfo user;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private UUID id;
        private String username;
        private String email;
        private String role;
        private Boolean emailVerified;
    }
}