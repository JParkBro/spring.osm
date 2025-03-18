package com.example.onlineshoppingmall.service;

import com.example.onlineshoppingmall.domain.CategoryEntity;
import com.example.onlineshoppingmall.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public List<CategoryEntity> getAllCategories() {
        return categoryMapper.findAll();
    }

}
