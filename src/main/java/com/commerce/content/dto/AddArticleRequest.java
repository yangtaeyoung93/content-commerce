package com.commerce.content.dto;

import com.commerce.content.domain.Article;
import com.commerce.content.domain.User;
import com.commerce.content.domain.tag.Tag;
import com.commerce.content.repository.TagRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    private String title;
    private String content;
    private String tagNames;

    public Article toEntity(User user){
        Article article = Article.builder()
                .title(title)
                .content(content)
                .build();
        article.setUser(user);
        return article;
    }
}
