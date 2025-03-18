package com.example.onlineshoppingmall.mapper;

import com.example.onlineshoppingmall.domain.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminCategoryMapper {

    List<CategoryEntity> getAllCategories();

    int insertCategory(CategoryEntity categoryEntity);

    int deleteCategory(CategoryEntity categoryEntity);

    int updateCategory(CategoryEntity categoryEntity);

    List<CategoryEntity> findCategoriesByIds(List<Long> categoryIds);

    CategoryEntity findCategoryById(Long id);
}
