package com.commerce.content.security;

import com.commerce.content.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.net.Authenticator;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userDetailService;


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
            return http.authorizeHttpRequests(auth -> auth.requestMatchers(
                            new AntPathRequestMatcher("/login"),
                            new AntPathRequestMatcher("/signup"),
                            new AntPathRequestMatcher("/user")
                    ).permitAll().anyRequest().authenticated())
                    .formLogin(formLogin -> formLogin.loginPage("/login")
                            .defaultSuccessUrl("/articles"))
                    .logout(logout -> logout.logoutSuccessUrl("/login")
                            .invalidateHttpSession(true))
                    .csrf(AbstractHttpConfigurer::disable)
                    .build()
            ;
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
