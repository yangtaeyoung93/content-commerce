package com.commerce.content.service;

import com.commerce.content.config.jwt.TokenProvider;
import com.commerce.content.domain.RefreshToken;
import com.commerce.content.domain.User;
import com.commerce.content.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    @Transactional
    public String createRefreshToken(User user, Duration expiry){
        String newRefreshToken = tokenProvider.generateRefreshToken(user, Duration.ofDays(14));
        if (refreshTokenRepository.findByUserId(user.getUserId()).isPresent()) {
            RefreshToken refreshToken = new RefreshToken(user.getUserId(), newRefreshToken);
        }else{
            refreshTokenRepository.save(new RefreshToken(user.getUserId(), newRefreshToken));
        }

        return newRefreshToken;
    }

    @Transactional
    public void deleteByUserId(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
