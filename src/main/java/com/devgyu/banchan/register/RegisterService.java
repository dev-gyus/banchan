package com.devgyu.banchan.register;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import com.devgyu.banchan.commons.email.MailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

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
        Account account = modelMapper.map(registerDto, Account.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAddress(address);
        account.setRole(Roles.ROLE_USER);
        Account save = registerRepository.save(account);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText(account.getNickname() + "님의 회원가입을 진심으로 축하합니다.");
        mailService.send(mailMessage);
        return save;
    }
}
