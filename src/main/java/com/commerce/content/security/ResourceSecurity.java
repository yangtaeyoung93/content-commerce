package com.commerce.content.security;

import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.repository.ArticleRepository;
import com.commerce.content.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResourceSecurity {

    private final ArticleRepository articleRepository;

    public boolean isArticleOwner(Long articleIdx, Authentication authentication) {
        log.info("소유자 체크 들어옴: articleId={}, user={}", articleIdx, authentication.getName());
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return articleRepository.findById(articleIdx).map(
                o -> o.getUser().getRole().name().equals(user.getUser().getRole().name())
        ).orElse(false);
    }
}
