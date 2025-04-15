package com.commerce.content.controller.article;

import com.commerce.content.domain.Article;
import com.commerce.content.domain.User;
import com.commerce.content.dto.AddArticleRequest;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.service.ArticleService;
import com.commerce.content.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleRestController {
    private final ArticleService articleService;
    private final UserService userService;

    @PostMapping("/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = userService.getUserWithPosts(customUserDetails.getUser());
        Article savedArticle = articleService.save(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }
}
