package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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

    private final UserService userService;

    public String generateAccessToken(UUID userId, String emailAddress){
        Map<String, Object> claims = new HashMap<>();
        return createAccessToken(claims, userId, emailAddress);
    }

    public String generateRefreshToken(UUID userId, String emailAddress) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims,userId, emailAddress);
    }

    public User getUser(String token) {
        String email = extractEmailAddress(token);
        return userService.getUserByEmailOrThrow(email);
    }

    private String createRefreshToken(Map<String, Object> claims, UUID userId, String emailAddress) {
        long expiration = refreshTokenExpiration;
        return createToken(claims, userId, emailAddress, expiration);
    }

    private String createAccessToken(Map<String, Object> claims, UUID userId, String emailAddress) {
        long expiration = accessTokenExpiration;
        return createToken(claims, userId, emailAddress, expiration);
    }

    private String createToken(Map<String, Object> claims, UUID userId, String emailAddress, Long expiration){
        return Jwts
                .builder()
                .claims(claims)
                .subject(userId.toString())
                .claim("email", emailAddress)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Fix BASE64 decoding
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public String extractEmailAddress(String token) {
        return (String) extractClaim(token, claims -> claims.get("email"));
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
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UUID userId) throws ExpiredJwtException {
        final UUID extractedUserId = extractUserId(token);
        return extractedUserId.toString().equals(userId.toString());
    }

}
