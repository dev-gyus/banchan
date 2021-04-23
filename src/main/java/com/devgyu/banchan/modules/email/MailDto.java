package com.devgyu.banchan.modules.email;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailDto {
    private String to;
    private String subject;
    private String message;

    public MailDto(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
}
