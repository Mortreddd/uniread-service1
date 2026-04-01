package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.dto.response.auth.LoginResponse;

public interface OAuthService {
    LoginResponse handleOAuthLogin(String accessToken);
}
