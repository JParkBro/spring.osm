package com.example.onlineshoppingmall.mapper;

import com.example.onlineshoppingmall.domain.OrderEntity;
import com.example.onlineshoppingmall.domain.OrderItemsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    int insertOrder(OrderEntity order);

    int insertOrderItem(OrderItemsEntity itemsEntity);

    List<OrderEntity> findOrdersByUserId(Map<String, Object> params);

    int totalCount(Map<String, Object> params);

    OrderEntity findOrderByOrderId(String orderId);

    List<OrderItemsEntity> findOrderItemsByOrderId(String orderId);
}
