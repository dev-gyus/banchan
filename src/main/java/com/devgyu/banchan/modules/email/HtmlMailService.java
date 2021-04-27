package com.devgyu.banchan.modules.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Profile({"real", "dev"})
@Component
@RequiredArgsConstructor
public class HtmlMailService implements MailService{
    private final JavaMailSender javaMailSender;

    @Override
    public void doSend(MailDto mailDto){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(mailDto.getTo());
            mimeMessageHelper.setSubject(mailDto.getSubject());
            mimeMessageHelper.setText(mailDto.getMessage(), true);
            javaMailSender.send(mimeMessage);
            log.error("MailSend Success");
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("MailSend Failed", e);
        }

    }
}
