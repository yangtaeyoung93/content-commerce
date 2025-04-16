package com.commerce.content.dto;

import com.commerce.content.domain.Article;
import com.commerce.content.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateArticleRequest {
    private String title;
    private String content;
    private String tagNames;
}
