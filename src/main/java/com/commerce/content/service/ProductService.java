package com.commerce.content.service;

import com.commerce.content.domain.Product;
import com.commerce.content.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    public List<Product> findAll() {
        return productRepository.findAllByProductAndTags().orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public Product findById(Long productIdx) {
        return null;
    }
}
