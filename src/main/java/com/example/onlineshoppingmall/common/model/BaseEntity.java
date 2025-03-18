package com.example.onlineshoppingmall.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
    private String createdUser;
    private LocalDateTime createdAt;
    private String updatedUser;
    private LocalDateTime updatedAt;
    private String useYn;

    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.useYn = this.useYn == null ? "Y" : this.useYn;
    }

    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
