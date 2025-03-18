package com.example.onlineshoppingmall.dto;

import com.example.onlineshoppingmall.domain.Code;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {
    private String orderId;
    private LocalDate orderDate;
    private int totalPrice;

    private Code orderStatus;
    private ProductInfoDTO productInfo;
}
