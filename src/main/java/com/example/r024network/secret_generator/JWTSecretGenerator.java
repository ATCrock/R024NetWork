package com.example.r024network.secret_generator;
import java.security.SecureRandom;
import java.util.Base64;

public class JWTSecretGenerator {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String secret = Base64.getEncoder().encodeToString(bytes);
        System.out.println(secret);
    }
}

