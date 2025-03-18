package com.example.onlineshoppingmall.service;

import com.example.onlineshoppingmall.domain.CartItemEntity;
import com.example.onlineshoppingmall.domain.UserEntity;
import com.example.onlineshoppingmall.dto.CartItemDTO;
import com.example.onlineshoppingmall.dto.ProductInfoDTO;
import com.example.onlineshoppingmall.dto.UserDTO;
import com.example.onlineshoppingmall.mapper.AuthMapper;
import com.example.onlineshoppingmall.mapper.CartItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPageService {

    private final AuthMapper authMapper;
    private final CartItemMapper cartItemMapper;

    public List<CartItemDTO> getCartItems(String userId) {
        List<CartItemEntity> entities = cartItemMapper.findCartItemsByUserId(userId);

        List<CartItemDTO> dtoList = new ArrayList<>();
        for (CartItemEntity entity : entities) {
            CartItemDTO dto = new CartItemDTO();
            dto.setId(entity.getId());
            dto.setQuantity(entity.getQuantity());

            // ProductInfoDTO 가져오기,
            ProductInfoDTO infoDTO = cartItemMapper.findProductInfoByProductId(entity.getProductId());
            infoDTO.setThumbnail(cartItemMapper.findProductImageByProductId(entity.getProductId()));
            dto.setProductInfo(infoDTO);

            // add dtoList
            dtoList.add(dto);
        }

        return dtoList;
    }

    public UserEntity getUser(String userId) {
        return authMapper.getUserById(userId);
    }

    public List<CartItemDTO> getCartItems(List<Long> selectedItemIds) {
        List<CartItemDTO> dtoList = new ArrayList<>();

        for (Long id : selectedItemIds) {
            CartItemDTO dto = new CartItemDTO();
            CartItemEntity entity = cartItemMapper.findCartItemById(id);
            dto.setId(entity.getId());
            dto.setQuantity(entity.getQuantity());

            ProductInfoDTO infoDTO = cartItemMapper.findProductInfoByProductId(entity.getProductId());
            infoDTO.setThumbnail(cartItemMapper.findProductImageByProductId(entity.getProductId()));

            dto.setProductInfo(infoDTO);
            dtoList.add(dto);
        }

        return dtoList;
    }

    public List<CartItemDTO> getDirectOrderItem(Long productId, int quantity) {
        List<CartItemDTO> dtoList = new ArrayList<>();

        CartItemDTO dto = new CartItemDTO();
        // 임시 ID 설정 (또는 필요에 따라 -1 같은 특수값 사용)
        dto.setId(-1L); // 장바구니에 저장되지 않은 상품임을 표시
        dto.setQuantity(quantity);

        ProductInfoDTO infoDTO = cartItemMapper.findProductInfoByProductId(productId);
        infoDTO.setThumbnail(cartItemMapper.findProductImageByProductId(productId));

        dto.setProductInfo(infoDTO);
        dtoList.add(dto);

        return dtoList;
    }

}
