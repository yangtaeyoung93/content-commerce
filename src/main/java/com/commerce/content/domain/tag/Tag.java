package com.commerce.content.domain.tag;

import com.commerce.content.domain.Article;
import com.commerce.content.domain.Product;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag {
    @Id @GeneratedValue
    @Column(name = "tag_idx")
    private Long id;

    @Column(unique = true)
    private String name;


    @ManyToMany
    @JoinTable(name = "product_tag"
            , joinColumns = @JoinColumn(name = "tag_idx")
            , inverseJoinColumns = @JoinColumn(name = "product_idx"))
    private List<Product> products = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "article_tag"
            , joinColumns = @JoinColumn(name = "tag_idx")
            , inverseJoinColumns = @JoinColumn(name = "article_idx"))
    private List<Article> articles = new ArrayList<>();

}
