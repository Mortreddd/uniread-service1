package com.bsit.uniread.application.services.auth;

import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JsonWebTokenService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    private final UserService userService;

    public String generateToken(String emailAddress){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, emailAddress);
    }

    public User getUser(String token) {
        String email = extractEmailAddress(token);
        return userService.getUserByEmail(email);
    }

    private String createToken(Map<String, Object> claims, String emailAddress){
        long expirationTime = DateUtil.JSON_WEB_TOKEN_EXPIRATION;
        return Jwts
                .builder()
                .claims(claims)
                .subject(emailAddress)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() * expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Fix BASE64 decoding
        return Keys.hmacShaKeyFor(keyBytes);
//        return secretKey;
    }

    public String extractEmailAddress(String token) {
        return extractClaim(token, Claims::getSubject);
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

    public boolean validateToken(String token, UserDetails userDetails) throws ExpiredJwtException {
        final String username = extractEmailAddress(token);
        return username.equals(userDetails.getUsername()) || isTokenExpired(token);
    }

}
