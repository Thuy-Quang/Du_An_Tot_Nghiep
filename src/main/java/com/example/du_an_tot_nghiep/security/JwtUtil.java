package com.example.du_an_tot_nghiep.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "9d23bcd7121b4e37a8f67e8c5a774301d3f6b2bb35c85a05d5c531947f1b8baf";

    public String generateToken(String username, Long userId, List<String> roles) {
        /**
         * Truyền danh sách quyền (roles) từ database vào token.
         * Role phải được viết theo định dạng ROLE_*
         */
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 giờ
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public List<SimpleGrantedAuthority> extractClaims(String token) {
        return ((List<String>) Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("roles"))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
