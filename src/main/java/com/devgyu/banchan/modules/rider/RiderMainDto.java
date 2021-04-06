package com.devgyu.banchan.modules.rider;

import com.devgyu.banchan.account.Address;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RiderMainDto {
    private Long id;
    private String email;
    private String driverLicense;

    @NotBlank
    @Pattern(regexp = "[가-힣0-9A-Za-z]{2,10}",
            message = "닉네임은 특수문자 제외 최소 1글자 이상 최대 10글자 미만으로 입력해주세요")
    private String nickname;
    private String password;
    @NotBlank
    @Pattern(regexp = "\\d{10,11}",
            message = "올바른 휴대폰 번호를 입력해 주세요")
    private String phone;
    @NotBlank
    @Pattern(regexp = "[가-힣]{2,10}")
    private String name;
    @NotBlank
    private String zipcode;
    @NotBlank
    private String road;
    @NotBlank
    private String jibun;
    @NotBlank
    private String detail;

    private String thumbnail;

    private String extra;

    public RiderMainDto(Long id, String driverLicense, String email, String nickname, String password, String phone, String name) {
        this.id = id;
        this.driverLicense = driverLicense;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.phone = phone;
        this.name = name;
    }
}
