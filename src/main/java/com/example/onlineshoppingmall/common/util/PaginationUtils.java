package com.example.onlineshoppingmall.common.util;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public class PaginationUtils {

    public static void setPageAttributes(Model model, Page<?> page, int currentPage) {
        int pageDisplayCount = 5;
        int startPage = Math.max(1, currentPage - (pageDisplayCount / 2));
        int endPage = Math.min(page.getTotalPages(), startPage + pageDisplayCount - 1);

        if (endPage == page.getTotalPages()) {
            startPage = Math.max(1, endPage - pageDisplayCount + 1);
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
    }

    public static void setPageAttributes(Model model, int totalItems, int currentPage, int pageSize) {
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int pageDisplayCount = 5;
        int startPage = Math.max(1, currentPage - (pageDisplayCount / 2));
        int endPage = Math.min(totalPages, startPage + pageDisplayCount - 1);

        if (endPage == totalPages) {
            startPage = Math.max(1, endPage - pageDisplayCount + 1);
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
    }
}
