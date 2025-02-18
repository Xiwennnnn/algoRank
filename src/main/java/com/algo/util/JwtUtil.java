package com.algo.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.springframework.security.crypto.encrypt.Encryptors;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET_KEY = "#da3f9876";

    public static String generateToken(String username, String role) {
        Map<String, Object> payload = Map.of("username", username, "role", role);
        return JWTUtil.createToken(payload, SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static String getUsername(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return (String) jwt.getPayload("username");
    }

    public static String getRole(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return (String) jwt.getPayload("role");
    }
}
