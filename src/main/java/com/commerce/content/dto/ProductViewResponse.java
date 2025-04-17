package com.commerce.content.dto;

import com.commerce.content.domain.Product;
import com.commerce.content.domain.tag.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductViewResponse {

    private Long productIdx;
    private String name;
    private int price;
    private int stockQuantity;
    private List<Tag> tags = new ArrayList<>();


    public ProductViewResponse(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.tags = product.getTags();
    }
}
