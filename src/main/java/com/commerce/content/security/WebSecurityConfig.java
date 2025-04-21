package com.commerce.content.security;

import com.commerce.content.config.TokenAuthenticationFilter;
import com.commerce.content.config.jwt.TokenProvider;
import com.commerce.content.service.UserDetailService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.net.Authenticator;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig{

    private final TokenProvider tokenProvider;
    private final UserDetailService userDetailService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @PostConstruct
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
    /**
     * 스프링 시큐리티 기능 비활성화
     * @return
     */
    @Bean
    public WebSecurityCustomizer configuer(){
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    /**
     * 특정 HTTP 요청에 대한 웹 기반 보안 구성
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            // 토근 인증방식 로그인 변경
            return http.authorizeHttpRequests(auth -> auth.requestMatchers(
                            new AntPathRequestMatcher("/login"),
                            new AntPathRequestMatcher("/logout"),
                            new AntPathRequestMatcher("/signup"),
                            new AntPathRequestMatcher("/user")
                    ).permitAll()
                     .requestMatchers("/api/**").authenticated()
                            .anyRequest().permitAll()
                    )
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // 하이브리드 세션 추가 필수
                    .exceptionHandling(exception->exception
                            .authenticationEntryPoint(customAuthenticationEntryPoint)
                            .accessDeniedHandler(customAccessDeniedHandler))
                    .addFilterBefore(tokenAuthenticationFilter, SecurityContextPersistenceFilter.class)
                    .csrf(AbstractHttpConfigurer::disable)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 인증 관리자 관련 설정
     * @param http
     * @param bCryptPasswordEncoder
     * @param userDetailService
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }

    /**
     * 패스워드 인코더로 사용할 빈 등록
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
