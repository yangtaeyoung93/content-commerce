package com.commerce.content.controller.product;

import com.commerce.content.domain.Product;
import com.commerce.content.dto.ArticleViewResponse;
import com.commerce.content.dto.CustomUserDetails;
import com.commerce.content.dto.ProductViewResponse;
import com.commerce.content.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductViewController {

    private final ProductService productService;
    @GetMapping("/products")
    public String getProducts(@AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model){
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "/product/products";
    }

    @GetMapping("/new-product")
    public String newProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestParam(required = false,name = "id")Long productIdx,
                             Model model){
        if(productIdx == null){
            model.addAttribute("product", new ProductViewResponse());
        }else{
            Product product = productService.findById(productIdx);
            model.addAttribute("product", new ProductViewResponse(product));
        }

        return "/product/newProduct";
    }
}
