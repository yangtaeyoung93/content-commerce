package com.commerce.content.controller.product;

import com.commerce.content.dto.AddProductRequest;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.dto.ProductViewResponse;
import com.commerce.content.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductRestController {

    private final ProductService productService;

    @PostMapping("/products")
    public ProductViewResponse addProducts(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody AddProductRequest request) {

        



    }



}
