package com.commerce.content.controller.article;

import com.commerce.content.domain.Article;
import com.commerce.content.dto.ArticleListViewResponse;
import com.commerce.content.dto.ArticleViewResponse;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ArticleViewController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public String getArticles(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        List<ArticleListViewResponse> articles =
                articleService.getArticles().stream().map(ArticleListViewResponse::new).toList();

        model.addAttribute("articles", articles);
        model.addAttribute("user", userDetails.getUser());

        return "articleList";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model){
        if(id == null){
            model.addAttribute("article", new ArticleViewResponse());
        }else{
            Article article = articleService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }
}
