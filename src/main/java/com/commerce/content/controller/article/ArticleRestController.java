package com.commerce.content.controller.article;

import com.commerce.content.domain.Article;
import com.commerce.content.domain.User;
import com.commerce.content.dto.AddArticleRequest;
import com.commerce.content.dto.ArticleListViewResponse;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.dto.UpdateArticleRequest;
import com.commerce.content.service.ArticleService;
import com.commerce.content.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleRestController {
    private final ArticleService articleService;
    private final UserService userService;
    @GetMapping("/articles")
    public ResponseEntity<Map<String, List<ArticleListViewResponse>>> findById(
            HttpServletRequest request
            ,@AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, List<ArticleListViewResponse>> map = new ConcurrentHashMap<>();
        User user = userService.findByUserId(userDetails.getUser().getUserId());
        List<ArticleListViewResponse> userWithPosts = articleService.getUserWithPosts(user)
                .stream().map(ArticleListViewResponse::new).toList();
        map.put("articles", userWithPosts);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findByUserId(userDetails.getUser().getUserId());
        Article savedArticle = articleService.save(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleListViewResponse> updateArticle(@PathVariable("id")Long id,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody UpdateArticleRequest request){
        Article updateArticle = articleService.update(id, request);
        return ResponseEntity.ok().body(new ArticleListViewResponse(updateArticle));
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<ArticleListViewResponse> deleteArticle(@PathVariable("id")Long id,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails
                                                                 ){
        articleService.delete(id);
        return ResponseEntity.ok().build();
    }
}
