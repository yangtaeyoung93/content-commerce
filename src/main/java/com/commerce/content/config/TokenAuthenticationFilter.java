package com.commerce.content.config;

import com.commerce.content.config.jwt.TokenProvider;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *  로그인 요청 외의 모든 API 요청에 대해 사용자의 JWT 토큰을 검사하고 인증 여부를 판단할 때 사용
 *  인증 OK -> SecurityContext에 사용자 정보를 넣는다.
 */
@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer";

    @Bean
    public FilterRegistrationBean<TokenAuthenticationFilter> registerFilter(TokenAuthenticationFilter filter) {
        FilterRegistrationBean<TokenAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
        registration.setEnabled(false); // SecurityFilterChain 안에서만 실행하게
        return registration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String uri = request.getRequestURI();
        if (!uri.startsWith("/api")) {
            filterChain.doFilter(request, response); // API 외는 세션 인증
            return;
        }
        String token = getAccessToken(authorizationHeader);
        if (token!= null && tokenProvider.validToken(token)) {
            System.out.println("정상 토큰");
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication); //SecurityContext에 인증 정보 저장
            System.out.println("권한 목록:");

        }

        // 캐시 방지 헤더
        response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
        response.setHeader("Pragma","no-cache");
        response.setDateHeader("Expires", 0);

        filterChain.doFilter(request, response); // 다음 필터로 넘김
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
