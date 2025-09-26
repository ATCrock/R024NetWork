package com.example.r024network.jwt;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JWTTokenUtil {

    private static final String SECRET = "LmPkEAze5jbwndNh/INYLrUMu51113Mz3N8QejiVnuE=";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION_TIME = 86400000;
    @Getter
    public static class JwtValidationResult {
        private final boolean valid;
        private final String message;
        private final Claims claims;

        public JwtValidationResult(boolean valid, String message, Claims claims) {
            this.valid = valid;
            this.message = message;
            this.claims = claims;
        }

    }

    /**
     * 生成JWT Token
     */
    public static String generateToken(String userAccount, Integer userId) {
        return Jwts.builder()
                .setSubject(userAccount)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验证JWT Token
     */
    public JwtValidationResult validateJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return new JwtValidationResult(true, "Token is valid", claims);

        } catch (ExpiredJwtException e) {
            return new JwtValidationResult(false, "Token has expired", null);
        } catch (MalformedJwtException e) {
            return new JwtValidationResult(false, "Invalid token format", null);
        } catch (Exception e) {
            return new JwtValidationResult(false, "Token verification failed", null);
        }
    }

    /**
     * 从Token中提取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * 从Token中提取用户ID
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Integer.class);
    }
}