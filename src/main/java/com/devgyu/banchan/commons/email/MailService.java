package com.devgyu.banchan.commons.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailService extends JavaMailSenderImpl {
    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
      log.info(simpleMessage.getText());
    }
}
