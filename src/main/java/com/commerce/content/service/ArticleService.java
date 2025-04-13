package com.commerce.content.service;

import com.commerce.content.domain.Article;
import com.commerce.content.domain.User;
import com.commerce.content.domain.tag.Tag;
import com.commerce.content.dto.AddArticleRequest;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.repository.ArticleRepository;
import com.commerce.content.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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

    @Transactional
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
}
