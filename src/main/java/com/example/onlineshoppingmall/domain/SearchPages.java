package com.example.onlineshoppingmall.domain;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;

public class SearchPages {
    @Getter
    private int page;
    private int size;
    private int offset;
    private Map<String, Object> params;

    public SearchPages() {
        this.page = 1;
        this.size = 10;
        calculateOffset();
        this.params = new HashMap<>();
    }

    public SearchPages(int page, int size) {
        this.page = Math.max(1, page);
        this.size = Math.max(1, size);
        calculateOffset();
        this.params = new HashMap<>();
    }

    private void calculateOffset() {
        this.offset = this.size * (this.page - 1);
    }

    public Pageable toPageable() {
        return PageRequest.of(this.page - 1, this.size);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", this.page - 1);
        params.put("size", this.size);
        params.put("offset", this.offset);
        params.put("params", this.params);
        return params;
    }

    public void addParam(String key, Object value) {
        this.params.put(key, value);
    }

    public void setPage(int page) {
        this.page = Math.max(1, page);
        calculateOffset();
    }
}
