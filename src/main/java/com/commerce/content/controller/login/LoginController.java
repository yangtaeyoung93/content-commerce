package com.commerce.content.controller.login;

import com.commerce.content.config.jwt.TokenProvider;
import com.commerce.content.domain.User;
import com.commerce.content.dto.AddLoginRequest;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.service.RefreshTokenService;
import com.commerce.content.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authentication;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AddLoginRequest request){

        Authentication authenticate = authentication.authenticate(new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();

        User user = userService.findByUserId(userDetails.getUsername());
        String token = tokenProvider.generateToken(user, Duration.ofHours(1L));
        String refreshToken = refreshTokenService.createRefreshToken(user, Duration.ofDays(14));



        Map<String, String> result = Map.of("accessToken", token,"refreshToken",refreshToken);
        return ResponseEntity.ok(result);
    }
}
