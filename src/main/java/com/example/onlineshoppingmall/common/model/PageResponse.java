package com.example.onlineshoppingmall.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;
    private int startPage;
    private int endPage;

    public static <T> PageResponse<T> from(Page<T> page, int currentPage) {
        int pageDisplayCount = 5;
        int startPage = Math.max(1, currentPage - (pageDisplayCount / 2));
        int endPage = Math.min(page.getTotalPages(), startPage + pageDisplayCount - 1);

        if (endPage == page.getTotalPages() && endPage > pageDisplayCount) {
            startPage = Math.max(1, endPage - pageDisplayCount + 1);
        }

        return PageResponse.<T>builder()
                .content(page.getContent())
                .currentPage(currentPage)
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .size(page.getSize())
                .startPage(startPage)
                .endPage(endPage)
                .build();
    }
}
