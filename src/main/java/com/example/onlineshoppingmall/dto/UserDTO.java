package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;
    private String name;
    private String email;
    private String mobilePhone;
    private String address;
    private String detailAddress;
    private String postalCode;
    private boolean termsAgreed;
    private boolean privacyAgreed;
    private String verificationKey;
    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private String useYn;
    private String role;
}
