package com.commerce.content.controller.product;

import com.commerce.content.domain.User;
import com.commerce.content.dto.AddProductRequest;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.dto.ProductViewResponse;
import com.commerce.content.service.ProductService;
import com.commerce.content.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductRestController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/products")
    public ResponseEntity<Object> addProducts(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody AddProductRequest request) {
            productService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
