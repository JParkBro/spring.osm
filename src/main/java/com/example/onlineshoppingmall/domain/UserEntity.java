package com.example.onlineshoppingmall.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RedisHash(value = "Users", timeToLive = 2592000)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {
    @Id
    private String userId;              // ID
    private String role;                // ROLE
    private String password;            // PASSWORD
    @Getter
    private String name;                // 이름
    private String email;               // 이메일
    private String mobilePhone;         // 휴대폰번호
    private String postalCode;          // 우편번호
    private String address;             // 기본주소
    private String detailAddress;       // 상세주소
    private boolean termsAgreed;        // 약관 동의 여부
    private boolean privacyAgreed;      // 개인정보 처리방침 동의 여부
    private String verificationKey;     // DI 정보 저장 (본인인증 키)
    private String createdUser;         // 생성자 ID
    private String createdAt;           // 생성 시간
    private String updatedUser;         // 수정자 ID
    private String updatedAt;           // 수정 시간
    private String useYn;               // 사용유무

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}