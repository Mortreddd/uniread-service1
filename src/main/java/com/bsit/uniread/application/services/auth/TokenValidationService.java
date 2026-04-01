package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.infrastructure.handler.exceptions.auth.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenValidationService {

    @Value("${spring.security.oauth2.client.registration.provider.google.user-info-uri}")
    private String googleUserInfoUri;
    
    private final RestTemplate restTemplate;

    public GoogleUserInfoResponse validateGoogleToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new InvalidTokenException("Access token is missing");
        }
        
        try {
            String requestUri = String.format("%s?access_token=%s", googleUserInfoUri, accessToken);
            ResponseEntity<GoogleUserInfoResponse> response = restTemplate.exchange(
                    requestUri,
                    HttpMethod.GET,
                    null,
                    GoogleUserInfoResponse.class
            );
            
            if (!response.hasBody() || response.getBody() == null) {
                throw new InvalidTokenException("Invalid or expired Google token");
            }
            
            GoogleUserInfoResponse userInfo = response.getBody();
            log.debug("Validated Google token for user: {}", userInfo.getEmail());

            return userInfo;
            
        } catch (RestClientException e) {
            log.error("Failed to validate Google token", e);
            throw new InvalidTokenException("Failed to validate Google token: " + e.getMessage(), e);
        }
    }
}