package com.example.onlineshoppingmall.service;

import com.example.onlineshoppingmall.domain.OrderConverter;
import com.example.onlineshoppingmall.domain.OrderEntity;
import com.example.onlineshoppingmall.domain.OrderItemsEntity;
import com.example.onlineshoppingmall.dto.OrderCompleteDTO;
import com.example.onlineshoppingmall.dto.OrderCompleteItemDTO;
import com.example.onlineshoppingmall.dto.OrdersDTO;
import com.example.onlineshoppingmall.dto.ProductInfoDTO;
import com.example.onlineshoppingmall.mapper.CartItemMapper;
import com.example.onlineshoppingmall.mapper.OrderMapper;
import com.example.onlineshoppingmall.common.util.CodeUtils;
import com.example.onlineshoppingmall.common.util.OrderIdGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderMapper orderMapper;
    private final CodeUtils codeUtils;
    private final CartItemMapper cartItemMapper;
    private final ProductService productService;

    private static final String PAYMENT_COMPLETED = "PAYMENT_COMPLETED";

    public String processOrder(OrderCompleteDTO dto) {
        OrderEntity entity = OrderConverter.convertOrderDtoToEntity(dto);

        Long paymentCompletedStatusId = codeUtils.getOrderStatusCodeId(PAYMENT_COMPLETED);
        entity.setCodeId(paymentCompletedStatusId);

        // 주문번호 생성
        String orderId = OrderIdGenerator.generateOrderId(dto.getPaymentMethod(), dto.getSimplePaymentType());
        entity.setOrderId(orderId);

        // Order Date 설정
        String dateStr = orderId.substring(0, 14);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime orderDate = LocalDateTime.parse(dateStr, formatter);
            entity.setOrderDate(orderDate);
        } catch (DateTimeParseException e) {
            logger.error("주문번호에서 날짜 파싱 실패: {}", orderId, e);
            entity.setOrderDate(LocalDateTime.now());
        }

        int result = orderMapper.insertOrder(entity);

        if (result == 1) {
            for (OrderCompleteDTO.OrderItemDTO item : dto.getOrderItems()) {
                OrderItemsEntity itemsEntity = OrderConverter.convertItemDtoToEntity(item);
                itemsEntity.setOrderId(orderId);

                int itemResult = orderMapper.insertOrderItem(itemsEntity);

                if (itemResult != 1) {
                    logger.error("주문 상품 삽입 실패: {}, 상품: {}", orderId, item.getProductId());
                }

                if (itemResult == 1 && item.getCartId() != null) {
                    cartItemMapper.deleteCartItem(item.getCartId());
                }
            }
        } else {
            logger.error("주문 정보 삽입 실패: {}", orderId);
            throw new RuntimeException("주문 정보 저장에 실패했습니다.");
        }

        return orderId;
    }

    public Page<OrdersDTO> getAllOrders(Map<String, Object> params) {
        List<OrdersDTO> orders = new ArrayList<>();

        List<OrderEntity> userOrders = orderMapper.findOrdersByUserId(params);
        for (OrderEntity orderEntity : userOrders) {
            OrdersDTO ordersDTO = new OrdersDTO();
            ordersDTO.setOrderId(orderEntity.getOrderId());
            ordersDTO.setOrderDate(orderEntity.getOrderDate().toLocalDate());
            ordersDTO.setTotalPrice(orderEntity.getTotalProductPrice() + orderEntity.getShippingCost());
            ordersDTO.setOrderStatus(codeUtils.getCodeById(orderEntity.getCodeId()).orElse(null));

            List<OrderItemsEntity> itemsEntityList = orderMapper.findOrderItemsByOrderId(orderEntity.getOrderId());
            ProductInfoDTO infoDTO = productService.getProduct(itemsEntityList.get(0).getProductId());
            if (itemsEntityList.size() > 1) {
                infoDTO.setName(infoDTO.getName() + " 외 " + (itemsEntityList.size() - 1) + "건");
            } else {
                infoDTO.setName(infoDTO.getName());
            }
            ordersDTO.setProductInfo(infoDTO);

            orders.add(ordersDTO);
        }

        Pageable pageable = PageRequest.of(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("size").toString()));

        int totalCount = orderMapper.totalCount(params);

        return new PageImpl<>(orders, pageable, totalCount);
    }

    public OrderEntity getOrder(String orderId) {
        return orderMapper.findOrderByOrderId(orderId);
    }

    public List<OrderCompleteItemDTO> getOrderItems(String orderId) {
        List<OrderCompleteItemDTO> itemDTOs = new ArrayList<>();

        List<OrderItemsEntity> items = orderMapper.findOrderItemsByOrderId(orderId);
        for (OrderItemsEntity item : items) {
            OrderCompleteItemDTO completeItemDTO = new OrderCompleteItemDTO();
            completeItemDTO.setId(item.getId());
            completeItemDTO.setQuantity(item.getQuantity());
            completeItemDTO.setPrice(item.getProductPrice());

            ProductInfoDTO infoDTO = productService.getProduct(item.getProductId());
            completeItemDTO.setProductName(infoDTO.getName());
            completeItemDTO.setProductId(infoDTO.getId());
            completeItemDTO.setThumbnail(infoDTO.getImages().get(0).getImageUri());

            itemDTOs.add(completeItemDTO);
        }

        return itemDTOs;
    }
}
