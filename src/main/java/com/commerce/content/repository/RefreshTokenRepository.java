package com.commerce.content.repository;

import com.commerce.content.domain.RefreshToken;
import com.commerce.content.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUserId(String userId);

    Optional<RefreshToken> findByUserId(String userId);
}
