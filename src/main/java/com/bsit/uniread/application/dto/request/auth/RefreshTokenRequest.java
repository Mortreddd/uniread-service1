package com.bsit.uniread.application.dto.request.auth;

import lombok.Data;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String refreshToken;
}
