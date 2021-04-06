package com.devgyu.banchan.register;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.customer.Customer;
import com.devgyu.banchan.cart.Cart;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public Account register(RegisterDto registerDto){
        Address address = new Address(registerDto.getZipcode(), registerDto.getRoad(),
                registerDto.getJibun(), registerDto.getDetail(), registerDto.getExtra());
        Customer customer = new Customer(registerDto.getEmail(), registerDto.getNickname(), passwordEncoder.encode(registerDto.getPassword()),
                registerDto.getName(), registerDto.getPhone(), address, UUID.randomUUID().toString());
        Cart cart = new Cart(customer);
        Account save = registerRepository.save(customer);


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText(customer.getNickname() + "님의 회원가입을 진심으로 축하합니다.");
        mailService.send(mailMessage);
        return save;
    }

    public StoreOwner ownerRegister(OwnerRegisterDto ownerRegisterDto) {
        Address address = new Address(ownerRegisterDto.getZipcode(), ownerRegisterDto.getRoad(), ownerRegisterDto.getJibun(),
                ownerRegisterDto.getDetail(), ownerRegisterDto.getExtra());
        StoreOwner storeOwner = new StoreOwner(ownerRegisterDto.getEmail(), ownerRegisterDto.getName(), passwordEncoder.encode(ownerRegisterDto.getPassword()),
                ownerRegisterDto.getName(), ownerRegisterDto.getPhone(), address, ownerRegisterDto.getTel());
        Cart cart = new Cart(storeOwner);
        registerRepository.save(storeOwner);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText(storeOwner.getNickname() + "님의 회원가입을 진심으로 축하합니다.");
        mailService.send(mailMessage);
        return storeOwner;
    }

    public Rider riderRegister(RiderRegisterDto riderRegisterDto) {
        Address address = new Address(riderRegisterDto.getZipcode(), riderRegisterDto.getRoad(),
                riderRegisterDto.getJibun(), riderRegisterDto.getDetail(), riderRegisterDto.getExtra());

        Rider rider = new Rider(riderRegisterDto.getEmail(), riderRegisterDto.getNickname(),
                passwordEncoder.encode(riderRegisterDto.getPassword()), riderRegisterDto.getName(),
                riderRegisterDto.getPhone(), address, UUID.randomUUID().toString(), riderRegisterDto.getDriverLicense());

        Cart cart = new Cart(rider);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText(rider.getNickname() + "님의 회원가입을 진심으로 축하합니다.");
        mailService.send(mailMessage);
        return registerRepository.save(rider);
    }
}
