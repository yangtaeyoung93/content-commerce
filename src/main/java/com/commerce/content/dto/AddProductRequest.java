package com.commerce.content.dto;

import com.commerce.content.domain.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class AddProductRequest {

    @NotEmpty(message = "상품명은 필수 입니다.")
    private String name;
    private int price;
    private int stockQuantity;
    private String tagNames;


    public Product toEntity(){
        return Product.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }
}
