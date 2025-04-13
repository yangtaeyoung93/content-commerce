package com.commerce.content.dto;

import com.commerce.content.domain.Article;
import com.commerce.content.domain.tag.Tag;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticleListViewResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final List<Tag> tags;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.tags = article.getTags();
    }
}
