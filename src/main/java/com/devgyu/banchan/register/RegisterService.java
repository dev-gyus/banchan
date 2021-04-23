package com.devgyu.banchan.register;

import com.devgyu.banchan.AppProperties;
import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.customer.Customer;
import com.devgyu.banchan.cart.Cart;
import com.devgyu.banchan.modules.email.MailDto;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.email.MailService;
import com.devgyu.banchan.register.dto.OwnerRegisterDto;
import com.devgyu.banchan.register.dto.RegisterDto;
import com.devgyu.banchan.register.dto.RiderRegisterDto;
import com.devgyu.banchan.modules.rider.Rider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    public Account register(RegisterDto registerDto){
        Address address = new Address(registerDto.getZipcode(), registerDto.getRoad(),
                registerDto.getJibun(), registerDto.getDetail(), registerDto.getExtra());
        Customer customer = new Customer(registerDto.getEmail(), registerDto.getNickname(), passwordEncoder.encode(registerDto.getPassword()),
                registerDto.getName(), registerDto.getPhone(), address, UUID.randomUUID().toString());
        Cart cart = new Cart(customer);
        Account save = registerRepository.save(customer);


        mailSend(customer);
        return save;
    }

    public StoreOwner ownerRegister(OwnerRegisterDto ownerRegisterDto) {
        Address address = new Address(ownerRegisterDto.getZipcode(), ownerRegisterDto.getRoad(), ownerRegisterDto.getJibun(),
                ownerRegisterDto.getDetail(), ownerRegisterDto.getExtra());
        StoreOwner storeOwner = new StoreOwner(ownerRegisterDto.getEmail(), ownerRegisterDto.getName(), passwordEncoder.encode(ownerRegisterDto.getPassword()),
                ownerRegisterDto.getName(), ownerRegisterDto.getPhone(), address, ownerRegisterDto.getTel());
        Cart cart = new Cart(storeOwner);
        registerRepository.save(storeOwner);

        mailSend(storeOwner);
        return storeOwner;
    }

    public Rider riderRegister(RiderRegisterDto riderRegisterDto) {
        Address address = new Address(riderRegisterDto.getZipcode(), riderRegisterDto.getRoad(),
                riderRegisterDto.getJibun(), riderRegisterDto.getDetail(), riderRegisterDto.getExtra());

        Rider rider = new Rider(riderRegisterDto.getEmail(), riderRegisterDto.getNickname(),
                passwordEncoder.encode(riderRegisterDto.getPassword()), riderRegisterDto.getName(),
                riderRegisterDto.getPhone(), address, UUID.randomUUID().toString(), riderRegisterDto.getDriverLicense());

        Cart cart = new Cart(rider);

        mailSend(rider);
        return registerRepository.save(rider);
    }

    private void mailSend(Account account) {
        String url = appProperties.getHost() + "/email-auth/" + account.getEmailToken();
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("url", url);
        String message = templateEngine.process("mailTemplate/register", context);
        String subject = "반찬의 민족 회원가입 완료 메일입니다.";

        MailDto mailDto = new MailDto(account.getEmail(), subject, message);

        mailService.doSend(mailDto);
    }
}
