package com.commerce.content.service;

import com.commerce.content.domain.Article;
import com.commerce.content.domain.User;
import com.commerce.content.domain.tag.Tag;
import com.commerce.content.dto.AddArticleRequest;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.dto.UpdateArticleRequest;
import com.commerce.content.repository.ArticleRepository;
import com.commerce.content.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;

    public List<Article> getArticles(){
        return articleRepository.findAll();
    }

    public Article findById(Long id){
        return articleRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Not found"));
    }

    public List<Article> getUserWithPosts(User user) {
        return articleRepository.findByIdWithArticlesAndTags(user.getId()).orElseThrow(()->new IllegalArgumentException("not found user"));
    }
    public Article save(AddArticleRequest dto, User user) {
        Article article = dto.toEntity(user);
        String tagNames = dto.getTagNames();
        Arrays.stream(tagNames.split(",")).map(String::trim).filter(s->!s.isEmpty())
                        .forEach(name -> {
                            Tag tag = tagRepository.findByName(name).orElseGet(
                                    ()-> {
                                        return tagRepository.save(new Tag(name));
                                    });
                            article.addTag(tag);
                        });

        return articleRepository.save(article);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest dto) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found article"));
        article.update(dto);

        String tagNames = dto.getTagNames();
        article.getTags().clear();

        Arrays.stream(tagNames.split(",")).map(String::trim).filter(s->!s.isEmpty())
                .forEach(name -> {
                    Tag tag = tagRepository.findByName(name).orElseGet(
                            ()-> {
                                return tagRepository.save(new Tag(name));
                            });
                    article.addTag(tag);
                });
        return article;
    }

    public void delete(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found article"));
        article.getTags().clear();
        articleRepository.delete(article);
    }
}
