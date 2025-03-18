package com.example.onlineshoppingmall.mapper;

import com.example.onlineshoppingmall.domain.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryEntity> findAll();

    CategoryEntity findById(Long id);
}
