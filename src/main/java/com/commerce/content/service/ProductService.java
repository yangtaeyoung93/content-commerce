package com.commerce.content.service;

import com.commerce.content.domain.Product;
import com.commerce.content.domain.tag.Tag;
import com.commerce.content.dto.AddProductRequest;
import com.commerce.content.repository.ProductRepository;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final TagRepository tagRepository;
    public List<Product> findAll() {
        return productRepository.findAllByProductAndTags().orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public Product findById(Long productIdx) {
        return productRepository.findById(productIdx).orElseThrow(()->new IllegalArgumentException("not found Product"));
    }

    @Transactional
    public void save(AddProductRequest dto) {

        Product product = dto.toEntity();

        Arrays.stream(dto.getTagNames().split(",")).map(String::trim).filter(s->!s.isEmpty())
                .forEach(name -> {
                    Tag tag = tagRepository.findByName(name).orElseGet(
                            ()-> {
                                return tagRepository.save(new Tag(name));
                            });
                    product.addTag(tag);
                });

        productRepository.save(product);
    }
}
