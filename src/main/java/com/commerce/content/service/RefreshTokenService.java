package com.commerce.content.service;

import com.commerce.content.config.jwt.TokenProvider;
import com.commerce.content.domain.RefreshToken;
import com.commerce.content.domain.User;
import com.commerce.content.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public String createRefreshToken(User user, Duration expiry){
        String newRefreshToken = tokenProvider.generateRefreshToken(user, Duration.ofDays(14));


        RefreshToken save = refreshTokenRepository.save(new RefreshToken(user.getUserId(), newRefreshToken));


        return newRefreshToken;
    }

    public void deleteByUserId(Long id) {
        refreshTokenRepository.deleteById(id);
    }
}
