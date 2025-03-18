package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private String userId;
    private String name;
    private String email;               // 이메일
    private String mobilePhone;         // 휴대폰번호
    private String postalCode;          // 우편번호
    private String address;             // 기본주소
    private String detailAddress;       // 상세주소

    private String currentPassword;
    private String newPassword;
}
