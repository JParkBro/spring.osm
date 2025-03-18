package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    private String userId;
    private String name;
    private String email;
    private String mobilePhone;
    private String postalCode;
    private String address;
    private String detailAddress;

}
