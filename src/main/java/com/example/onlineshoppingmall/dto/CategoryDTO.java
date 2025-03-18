package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private Long codeId;
    private String name;
    private Long parentId;
    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private String useYn;
}
