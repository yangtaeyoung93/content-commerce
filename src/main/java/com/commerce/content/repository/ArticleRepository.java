package com.commerce.content.repository;

import com.commerce.content.domain.Article;
import com.commerce.content.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    @Query("SELECT a FROM Article a " +
            "JOIN FETCH a.tags " +
            "WHERE  a.user.id =:id")
    Optional<List<Article>> findByIdWithArticlesAndTags(Long id);
}
