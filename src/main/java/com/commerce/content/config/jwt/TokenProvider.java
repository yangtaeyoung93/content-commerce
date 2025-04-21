package com.commerce.content.config.jwt;

import com.commerce.content.domain.User;
import com.commerce.content.service.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailService userDetailService;

    public String generateToken(User user, Duration expireDt) {
        return makeToken(user, new Date(new Date().getTime() + expireDt.toMillis()));
    }

    private String makeToken(User user, Date expiry) {

        return Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .setSubject(user.getUserId())
                .claim("idx",user.getId())
                .claim("name",user.getUsername())
                .claim("roles",List.of("ROLE_" + user.getRole().name()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    /**
     * 토큰 유효성 검사
     * @param token
     * @return
     */

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 토큰 기반 인증 정보 조회
     * Spring Security의 인증 객체로 변환하는 과정
     */

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String userId = claims.getSubject();

        List<String> roles = claims.get("roles", List.class);
        roles = Optional.ofNullable(roles).orElse(List.of());

        List<SimpleGrantedAuthority> authorities =
                roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = userDetailService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token){
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateRefreshToken(User user, Duration expiry) {
        Date now = new Date();
        return makeToken(user, new Date(now.getTime() + expiry.toMillis()));
    }
}
