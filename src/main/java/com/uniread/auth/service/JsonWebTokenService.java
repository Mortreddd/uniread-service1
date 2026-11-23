package com.uniread.auth.service;

import com.uniread.auth.dto.response.LoginResponse;
import com.uniread.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JsonWebTokenService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.accessToken.expiration}")
    private Integer accessTokenExpiration;
    @Value("${security.jwt.refreshToken.expiration}")
    private Integer refreshTokenExpiration;

    public String generateAccessToken(UUID userId, LoginResponse loginResponse){
        Map<String, Object> claims = new HashMap<>();
        return createAccessToken(claims, loginResponse);
    }

    public String generateRefreshToken(UUID userId, LoginResponse loginResponse) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, loginResponse);
    }

    private String createRefreshToken(Map<String, Object> claims, LoginResponse loginResponse) {
        long expiration = refreshTokenExpiration;
        return createToken(claims, loginResponse, "refresh", expiration);
    }

    private String createAccessToken(Map<String, Object> claims, LoginResponse loginResponse) {
        long expiration = accessTokenExpiration;
        return createToken(claims, loginResponse, "access", expiration);
    }

    private String createToken(Map<String, Object> claims, LoginResponse loginResponse, String type, long expiration) {
        claims.put("id", loginResponse.getId());
        claims.put("email", loginResponse.getEmail());
        claims.put("username", loginResponse.getUsername());
        claims.put("displayName", loginResponse.getDisplayName());
        claims.put("roles", loginResponse.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        claims.put("emailVerifiedAt", instantToString(loginResponse.getEmailVerifiedAt()));
        claims.put("bannedAt", instantToString(loginResponse.getBannedAt()));
        claims.put("deletedAt", instantToString(loginResponse.getDeletedAt()));
        claims.put("unbannedAt", instantToString(loginResponse.getUnbannedAt()));
        // optionally: emailVerifiedAt, bannedAt, etc. if downstream needs them
        return Jwts.builder()
                .claims(claims)
                .subject(loginResponse.getId().toString())
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Fix BASE64 decoding
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public boolean isAccessToken(String token)  {
        return "access".equals(extractClaim(token, c -> c.get("type", String.class)));
    }
    public boolean isRefreshToken(String token) {
        return "refresh".equals(extractClaim(token, c -> c.get("type", String.class)));
    }

    private String instantToString(Instant instant) {
        return instant != null ? instant.toString() : null;
    }
}
