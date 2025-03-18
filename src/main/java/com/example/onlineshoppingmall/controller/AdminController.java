package com.example.onlineshoppingmall.controller;

import com.example.onlineshoppingmall.domain.SearchPages;
import com.example.onlineshoppingmall.dto.CategoryDTO;
import com.example.onlineshoppingmall.dto.ProductFormDTO;
import com.example.onlineshoppingmall.dto.ProductResponseDTO;
import com.example.onlineshoppingmall.dto.UserDTO;
import com.example.onlineshoppingmall.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/login")
    public String showAdminLoginPage(Model model) {
        return "pages/admin/login";
    }

    @GetMapping("/layout")
    public String showAdminLayoutPage(Model model) {
        return "pages/admin/layout";
    }

    @GetMapping("/users/list")
    public String showAdminUserListPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        SearchPages params = new SearchPages(page, size);

        Page<UserDTO> userPage = adminService.getUsers(params.toMap());

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());

        int pageDisplayCount = 5; // 한 번에 표시할 페이지 번호 수
        int startPage = Math.max(1, page - (pageDisplayCount / 2));
        int endPage = Math.min(userPage.getTotalPages(), startPage + pageDisplayCount - 1);
        if (endPage == userPage.getTotalPages()) {
            startPage = Math.max(1, endPage - pageDisplayCount + 1);
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "pages/admin/users/list";
    }

    @GetMapping("/categories/form")
    public String showAdminCategoriesForm(Model model) {

        List<CategoryDTO> categories = adminService.getAllCategories();

        model.addAttribute("categories", categories);

        Map<Long, Integer> childCounts = new HashMap<>();
        for (CategoryDTO category : categories) {
            long count = categories.stream()
                    .filter(c -> category.getId().equals(c.getParentId()))
                    .count();
            childCounts.put(category.getId(), (int) count);
        }
        model.addAttribute("childCounts", childCounts);

        return "pages/admin/categories/form";
    }

    @GetMapping("/products/list")
    public String showAdminProductListPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            HttpSession session,
            Model model) {

        session.setAttribute("currentPage", page);

        SearchPages params = new SearchPages(page, size);

        Page<ProductResponseDTO> productPage = adminService.getProducts(params.toMap());

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());

        int pageDisplayCount = 5; // 한 번에 표시할 페이지 번호 수
        int startPage = Math.max(1, page - (pageDisplayCount / 2));
        int endPage = Math.min(productPage.getTotalPages(), startPage + pageDisplayCount - 1);
        if (endPage == productPage.getTotalPages()) {
            startPage = Math.max(1, endPage - pageDisplayCount + 1);
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "pages/admin/products/list";
    }

    // 상품 등록 폼
    @GetMapping("/products/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new ProductFormDTO());

        List<CategoryDTO> categories = adminService.getAllCategories();
        model.addAttribute("categories", categories);

        return "pages/admin/products/form";
    }

    // 상품 상세 폼
    @GetMapping("/products/detail/{id}")
    public String showProductDetailForm(@PathVariable Long id, Model model) {

        ProductResponseDTO dto = adminService.getProductById(id);
        model.addAttribute("product", dto);

        return "pages/admin/products/detail";
    }

    // 상품 등록 폼
    @GetMapping("/products/update/{id}")
    public String updateProductForm(@PathVariable Long id, Model model) {

        ProductResponseDTO dto = adminService.getProductById(id);
        model.addAttribute("product", dto);

        List<CategoryDTO> categories = adminService.getAllCategories();
        model.addAttribute("categories", categories);

        return "pages/admin/products/form";
    }
}
