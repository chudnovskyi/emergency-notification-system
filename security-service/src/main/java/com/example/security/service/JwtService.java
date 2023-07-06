package com.example.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${security.jwt.key}")
    private String key;

    @Value("${security.jwt.prefix}")
    public String prefix;

    public String generateJwt(UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), HS256)
                .compact();
    }

    public String extractJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(prefix)) {
            return authHeader.substring(prefix.length());
        }
        return null;
    }

    public boolean isJwtValid(String jwt, UserDetails userDetails) {
        String username = extractEmail(jwt);
        return username.equals(userDetails.getUsername()) && !isJwtExpired(jwt);
    }

    public String extractEmail(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isJwtExpired(String jwt) {
        return extractClaim(jwt, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
