package com.example.onlineshoppingmall.domain;

import com.example.onlineshoppingmall.dto.OrderCompleteDTO;

public class OrderConverter {

    public static OrderEntity convertOrderDtoToEntity(OrderCompleteDTO dto) {
        return OrderEntity.builder()
                .userId(dto.getUserId())
                .recipientName(dto.getRecipient())
                .phone(dto.getMobilePhone())
                .postalCode(dto.getPostalCode())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .deliveryRequest(dto.getDeliveryRequest())
                .totalProductPrice(dto.getTotalProductPrice())
                .shippingCost(dto.getShippingCost())
                .build();
    }

    public static OrderItemsEntity convertItemDtoToEntity(OrderCompleteDTO.OrderItemDTO dto) {
        return OrderItemsEntity.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .productPrice(dto.getPrice())
                .build();
    }
}
