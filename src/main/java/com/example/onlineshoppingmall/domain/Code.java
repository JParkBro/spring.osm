package com.example.onlineshoppingmall.domain;

import lombok.Data;

@Data
public class Code {
    private Long id;
    private String codeType;
    private String codeKey;
    private String codeValue;
    private String description;
    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private String useYn;
}
