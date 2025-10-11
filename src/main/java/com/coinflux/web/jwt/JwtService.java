package com.coinflux.web.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // ✅ Generate token with username, expiry, and type
    public String generateToken(String username, long expireInMinutes, TokenType type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type.name());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireInMinutes * 60 * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Unified validation method — checks all aspects
    public boolean isTokenValid(String token, String expectedUsername, TokenType expectedType) {
        try {
            Claims claims = extractAllClaims(token);

            String actualUsername = claims.getSubject();
            String actualType = (String) claims.get("type");
            Date expiration = claims.getExpiration();

            return actualUsername.equals(expectedUsername)
                    && expectedType.name().equals(actualType)
                    && expiration.after(new Date());
        } catch (Exception ex) {
            // Includes signature issues, malformed token, etc.
            return false;
        }
    }

    // ✅ Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    // ✅ Internal: extract all claims safely
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Internal: signing key
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
