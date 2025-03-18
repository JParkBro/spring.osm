package com.example.onlineshoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterDTO {

    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상, 20자 이하로 입력해야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상 입력해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Size(max = 20, message = "이름은 최대 20자까지 입력할 수 있습니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력해야 합니다.")
    @Size(max = 100, message = "이름은 최대 100자까지 입력할 수 있습니다.")
    private String email;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 000-0000-0000 형식으로 입력해야 합니다.")
    private String mobilePhone;

    private String postalCode;

    @Size(max = 255, message = "주소는 최대 255자까지 입력할 수 있습니다.")
    private String address;

    private String detailAddress;

    private boolean termsAgreed;

    private boolean privacyAgreed;

    private String verificationKey;
}
