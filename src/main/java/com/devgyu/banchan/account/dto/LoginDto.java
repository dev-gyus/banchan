package com.devgyu.banchan.account.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginDto {
    @NotBlank
    @Email(message = "올바른 이메일을 입력해 주세요")
    private String email;
    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]{4,20}", message = "특수문자를 제외한 영문,숫자 4~20 범위의 값을 입력해주세요.")
    private String password;

    private boolean rememberLogin;
}
