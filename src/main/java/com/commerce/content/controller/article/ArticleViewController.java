package com.commerce.content.controller.article;

import com.commerce.content.domain.Article;
import com.commerce.content.domain.User;
import com.commerce.content.dto.ArticleListViewResponse;
import com.commerce.content.dto.ArticleViewResponse;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ArticleViewController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public String getArticles(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("loginUser");
        if(user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "articleList";
    }

    @GetMapping("/new-article")
    public String newArticle(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestParam(required = false) Long id, Model model){
        if(id == null){
            model.addAttribute("article", new ArticleViewResponse());
        }else{
            Article article = articleService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model) {
        Article article = articleService.findById(id);
        model.addAttribute(article);
        return "article";
    }
}
