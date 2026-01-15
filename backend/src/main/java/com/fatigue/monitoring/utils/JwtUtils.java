package com.fatigue.monitoring.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return Token
     */
    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return generateToken(claims);
    }

    /**
     * 生成Token
     *
     * @param claims 自定义声明
     * @return Token
     */
    private String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token Token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? Long.valueOf(claims.get("userId").toString()) : null;
    }

    /**
     * 从Token中获取用户名
     *
     * @param token Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.get("username").toString() : null;
    }

    /**
     * 从Token中获取过期时间
     *
     * @param token Token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getExpiration() : null;
    }

    /**
     * 从Token中获取Claims
     *
     * @param token Token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("从Token中获取Claims失败：{}", e.getMessage());
            claims = null;
        }
        return claims;
    }

    /**
     * 验证Token是否有效
     *
     * @param token Token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null && !isTokenExpired(token);
        } catch (Exception e) {
            log.error("验证Token失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 判断Token是否过期
     *
     * @param token Token
     * @return 是否过期
     */
    private boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 刷新Token
     *
     * @param token Token
     * @return 新Token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + expiration));
        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 获取密钥
     *
     * @return 密钥
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
