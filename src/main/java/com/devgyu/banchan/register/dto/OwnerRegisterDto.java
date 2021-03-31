package com.devgyu.banchan.register.dto;

import com.devgyu.banchan.account.Address;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class OwnerRegisterDto {
    @NotBlank
    @Email(message = "올바른 이메일을 입력해주세요.")
    private String email;
    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]{4,20}", message = "영문, 숫자로만 4~20글자 이내로 작성해주세요")
    private String password;
    @NotBlank
    @Pattern(regexp = "[가-힣]{1}[가-힣A-Za-z0-9]{1,9}",
            message = "첫글자는 한글, 나머지글자는 한글,영어,숫자로 1~10글자이내로 작성해주세요")
    private String name;
    private Address address;
    @NotBlank
    @Pattern(regexp = "\\d{9,11}",
            message = "올바른 가게 전화번호를 입력해 주세요")
    private String tel;
    @NotBlank
    @Pattern(regexp = "\\d{10,11}",
            message = "올바른 휴대폰 번호를 입력해 주세요")
    private String phone;
    @NotBlank
    private String zipcode;
    @NotBlank
    private String road;
    @NotBlank
    private String jibun;
    @NotBlank
    private String detail;

    private String extra;
}
