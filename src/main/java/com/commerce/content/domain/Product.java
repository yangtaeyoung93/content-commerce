package com.commerce.content.domain;

import com.commerce.content.domain.tag.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_idx")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;


    @Builder
    public Product(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }


    @ManyToMany
    @JoinTable(name = "product_tag"
            , joinColumns = @JoinColumn(name = "product_idx")
            , inverseJoinColumns = @JoinColumn(name = "tag_idx"))
    private List<Tag> tags = new ArrayList<>();

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getProducts().add(this); // 양방향 유지!
    }

    /**
     * 재고 수량 증가
     * @param quantity
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * 재고 수량 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new RuntimeException();
        }

        this.stockQuantity = restStock;
    }
}
