package com.example.onlineshoppingmall.mapper;

import com.example.onlineshoppingmall.domain.ImageEntity;
import com.example.onlineshoppingmall.domain.ProductCategoryMappingEntity;
import com.example.onlineshoppingmall.domain.ProductEntity;
import com.example.onlineshoppingmall.dto.ProductResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminProductMapper {

    int totalCount(Map<String, Object> params);

    List<ProductResponseDTO> findProductsWithPagination(Map<String, Object> params);

    void insertProduct(ProductEntity entity);

    int insertProductMapping(ProductCategoryMappingEntity entity);

    Long findCategoryIdByProductId(Long productId);

    void insertImages(ImageEntity entity);

    ProductEntity getProductById(Long productId);

    List<ImageEntity> findByReferenceIdAndReferenceTypeOrderByDisplayOrder(Long productId, String referenceType);

    List<Map<String, Object>> getProductImagesForDelete(Long productId);

    void deleteProduct(Long productId);

    void deleteProductImages(Long productId);

    void updateProduct(ProductEntity productEntity);

    int updateProductMapping(ProductCategoryMappingEntity entity);

    void deleteImage(Long imageId);

    Integer getMaxImageOrder(Long productId);
}
