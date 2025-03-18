package com.example.onlineshoppingmall.controller;

import com.example.onlineshoppingmall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductPageController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public String showProductPage(@PathVariable("id") Long id, Model model) {

        model.addAttribute("products", productService.getProducts(id));
        model.addAttribute("hierarchies", productService.getCategoryHierarchy(id));

        return "pages/product/list/productList";
    }

    @GetMapping("/detail/{id}")
    public String showProductDetailPage(@PathVariable("id") Long id, Model model) {

        model.addAttribute("product", productService.getProduct(id));

        return "pages/product/detail/productDetail";
    }
}
