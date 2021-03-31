package com.devgyu.banchan.account.dto;

import lombok.Data;

@Data
public class ModifyPasswordDto {
    private String password;
    private String passwordRepeat;
}
