package com.example.r024network.jwt;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JWTTokenUtil {

    private static final String SECRET = "LmPkEAze5jbwndNh/INYLrUMu51113Mz3N8QejiVnuE=";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION_TIME = 86400000; // 过期时间一天

    public record JwtValidationResult(boolean valid, String message, Claims claims) {}

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

    // 验证token
    public JwtValidationResult validateJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(KEY) // 设置秘钥
                    .build()
                    .parseClaimsJws(token) // 导入token
                    .getBody();

            return new JwtValidationResult(true, "Token验证成功", claims);

        } catch (ExpiredJwtException e) {
            return new JwtValidationResult(false, "Token已过期", null);
        } catch (MalformedJwtException e) {
            return new JwtValidationResult(false, "Token不符合规范", null);
        } catch (Exception e) {
            return new JwtValidationResult(false, "Token验证失败", null);
        }
    }

    // 获取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 获取用户id
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Integer.class);
    }
}