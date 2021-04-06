package com.devgyu.banchan.register.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class RiderRegisterDto extends RegisterDto{
    @NotBlank
    @Pattern(regexp = "(\\d{2}-\\d{2}-\\d{6}-\\d{2})"
                , message = "올바른 운전면허 번호를 입력해주세요")
    private String driverLicense;

    public RiderRegisterDto(@NotBlank @Email(message = "올바른 이메일을 입력해주세요.") String email, @NotBlank @Pattern(regexp = "[가-힣0-9A-Za-z]{2,10}",
            message = "닉네임은 특수문자 제외 최소 1글자 이상 최대 10글자 미만으로 입력해주세요") String nickname, @NotBlank @Pattern(regexp = "[A-Za-z0-9]{4,20}",
            message = "비밀번호는 특수문자 제외 최소 4글자 이상 최대 20글자 미만으로 입력해주세요") String password, @NotBlank @Pattern(regexp = "[가-힣]{2,10}") String name, @NotBlank @Pattern(regexp = "\\d{10,11}",
            message = "올바른 휴대폰 번호를 입력해 주세요") String phone, @NotBlank String zipcode, @NotBlank String road, @NotBlank String jibun, @NotBlank String detail, String extra, @NotBlank @Pattern(regexp = "(?<=[^0-9a-zA-Z])(\\d{2}[\\s-:\\.])(\\d{6}[\\s-:\\.])(\\d{2})(?=[^0-9a-zA-Z])"
            , message = "올바른 운전면허 번호를 입력해주세요") String driverLicense) {
        super(email, nickname, password, name, phone, zipcode, road, jibun, detail, extra);
        this.driverLicense = driverLicense;
    }
}
