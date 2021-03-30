package com.devgyu.banchan.account;

import lombok.Data;

@Data
public class ModifyPasswordDto {
    private String password;
    private String passwordRepeat;
}
