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
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailService userDetailService;

    public String generateToken(User user, Duration expireDt) {
        Date now = new Date();
        return makeToken(user, new Date(now.getTime() + expireDt.toMillis()));
    }

    private String makeToken(User user, Date expiry) {
        Date now = new Date();

        return Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getUserId())
                .claim("idx",user.getId())
                .claim("name",user.getUsername())
                .claim("role",user.getRole().name())
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
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        String userId = getUserId(token);

        UserDetails userDetails = userDetailService.loadUserByUsername(userId);
        //return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
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
