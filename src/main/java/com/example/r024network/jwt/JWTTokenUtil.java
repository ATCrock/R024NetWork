package com.example.r024network.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTTokenUtil {
    @Value("${jwt.secret}") // 从配置文件中读取密钥
    private String secret;

    /**
     * 验证 JWT Token
     * @param token JWT Token
     * @return 验证结果和声明信息
     */
    public JwtValidationResult validateJWT(String token) {
        try {
            // 使用密钥验证 Token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 检查 Token 是否过期
            if (claims.getExpiration().before(new Date())) {
                return new JwtValidationResult(false, "Token expired", null);
            }

            return new JwtValidationResult(true, "Valid token", claims);

        } catch (ExpiredJwtException e) {
            return new JwtValidationResult(false, "Token expired", null);
        } catch (UnsupportedJwtException e) {
            return new JwtValidationResult(false, "Unsupported JWT", null);
        } catch (MalformedJwtException e) {
            return new JwtValidationResult(false, "Malformed JWT", null);
        } catch (SecurityException e) {
            return new JwtValidationResult(false, "Invalid signature", null);
        } catch (IllegalArgumentException e) {
            return new JwtValidationResult(false, "Invalid token", null);
        }
    }

    /**
     * 生成签名密钥
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * JWT 验证结果类
     */
    public static class JwtValidationResult {
        private final boolean isValid;
        private final String message;
        private final Claims claims;

        public JwtValidationResult(boolean isValid, String message, Claims claims) {
            this.isValid = isValid;
            this.message = message;
            this.claims = claims;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getMessage() {
            return message;
        }

        public Claims getClaims() {
            return claims;
        }
    }
}