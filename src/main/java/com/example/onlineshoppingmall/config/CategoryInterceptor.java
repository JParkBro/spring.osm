package com.example.onlineshoppingmall.config;

import com.example.onlineshoppingmall.domain.CategoryEntity;
import com.example.onlineshoppingmall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryInterceptor implements HandlerInterceptor {

    private final CategoryService categoryService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/admin/")) {
            return;
        }

        if (modelAndView != null) {
            List<CategoryEntity> categories = categoryService.getAllCategories();
            modelAndView.addObject("categories", categories);
        };
    }
}
