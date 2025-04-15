package com.commerce.content.controller.article;

import com.commerce.content.config.jwt.TokenProvider;
import com.commerce.content.domain.Article;
import com.commerce.content.domain.User;
import com.commerce.content.dto.AddArticleRequest;
import com.commerce.content.dto.ArticleListViewResponse;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.service.ArticleService;
import com.commerce.content.service.RefreshTokenService;
import com.commerce.content.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleRestController {
    private final ArticleService articleService;
    private final UserService userService;
    private final AuthenticationManager authentication;

    @GetMapping("/articles")
    public ResponseEntity<Map<String, List<ArticleListViewResponse>>> findById(HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Map<String, List<ArticleListViewResponse>> map = new ConcurrentHashMap<>();

        if (auth != null && auth.isAuthenticated()) {
            List<ArticleListViewResponse> articles =
                    articleService.getArticles().stream().map(ArticleListViewResponse::new).toList();
            map = Map.of("articles", articles);
        }


        return ResponseEntity.ok(map);
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = userService.getUserWithPosts(customUserDetails.getUser());
        Article savedArticle = articleService.save(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }
}
