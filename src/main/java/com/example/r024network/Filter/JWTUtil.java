package com.example.r024network.Filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
// 用于测试JWT的类
@Component
public class JWTUtil {
    // 秘钥
    private static final String SECRET = "LmPkEAze5jbwndNh/INYLrUMu51113Mz3N8QejiVnuE=";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String extractUsername(String token) {
        // 移除可能的"Bearer "前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 解析令牌并获取声明
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 从声明中获取用户名
        // 假设用户名存储在"sub" (subject) 字段中
        return claims.getSubject();

        // 或者如果你将用户名存储在自定义字段中，比如"username"
        // return claims.get("username", String.class);
    }

    // 验证令牌的有效性
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //-----------------------------以下为JWT生成调试
    public static String generateToken(String subject, String issuer, long expirationMs, Map<String, Object> customClaims) {
        // 如果没有提供自定义声明，创建一个空Map
        if (customClaims == null) {
            customClaims = new HashMap<>();
        }

        // 构建JWT
        return Jwts.builder()
                .setClaims(customClaims)          // 自定义声明
                .setSubject(subject)              // 主题（通常是用户名/用户ID）
                .setIssuer(issuer)                // 签发者
                .setIssuedAt(new Date())          // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs)) // 过期时间
                .signWith(KEY, SignatureAlgorithm.HS256) // 使用HS256算法和秘钥签名
                .compact();                       // 生成最终的Token字符串
    }

    /**
     * 生成Base64编码的秘钥（用于展示或存储）
     * @return Base64编码的秘钥字符串
     */
    public static String getBase64EncodedSecret() {
        return Base64.getEncoder().encodeToString(KEY.getEncoded());
    }


    public static void main(String[] args) {
        // 示例：创建一个包含用户信息的Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 12345);
        claims.put("role", "admin");
        claims.put("email", "user@example.com");

        // 生成Token，有效期为1小时
        String token = generateToken("john_doe", "MyApp", 3600000, claims);

        System.out.println("生成的JWT Token:");
        System.out.println(token);
        System.out.println("\nBase64编码的秘钥:");
        System.out.println(getBase64EncodedSecret());

        // 打印Token的三部分（用于调试和学习）
        String[] parts = token.split("\\.");
        System.out.println("\nToken分解:");
        System.out.println("Header: " + new String(Base64.getUrlDecoder().decode(parts[0])));
        System.out.println("Payload: " + new String(Base64.getUrlDecoder().decode(parts[1])));
        // 签名部分不需要解码，它是二进制数据
        System.out.println("Signature: " + parts[2]);
    }
    String simpleToken = this.generateToken("user123", "MyApp", 3600000, null);
    /*
        // 基本用法 - 只包含主题和签发者
    String simpleToken = JwtGenerator.generateToken("user123", "MyApp", 3600000, null);
                                                   subject   issuer  exptime(MS)  customClaims(hashmap)
    // 高级用法 - 包含自定义声明
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", 12345);
    claims.put("role", "admin");
    claims.put("email", "user@example.com");

    String advancedToken = JwtGenerator.generateToken("user123", "MyApp", 3600000, claims);
     */
}