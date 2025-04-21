package com.commerce.content.controller.login;

import com.commerce.content.config.jwt.TokenProvider;
import com.commerce.content.domain.User;
import com.commerce.content.dto.AddLoginRequest;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.service.RefreshTokenService;
import com.commerce.content.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity login(@RequestBody AddLoginRequest request, HttpServletRequest httpRequest){

        Authentication authenticate = authentication.authenticate(new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();
        User user = userService.findByUserId(userDetails.getUsername());

        httpRequest.getSession(true).setAttribute("loginUser", user);

        String token = tokenProvider.generateToken(user, Duration.ofHours(1L));
        String refreshToken = refreshTokenService.createRefreshToken(user, Duration.ofDays(14));

        Map<String, String> result = Map.of("accessToken", token,"refreshToken",refreshToken);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails userDetails) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate(); // 세션 제거
        }
        refreshTokenService.deleteByUserId(userDetails.getUser().getUserId());
        return "redirect:/login";
    }
}
