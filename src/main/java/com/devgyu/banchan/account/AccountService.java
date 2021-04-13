package com.devgyu.banchan.account;

import com.devgyu.banchan.AppProperties;
import com.devgyu.banchan.account.dto.ForgotDto;
import com.devgyu.banchan.account.dto.ModifyPasswordDto;
import com.devgyu.banchan.account.dto.MypageDto;
import com.devgyu.banchan.modules.email.MailService;
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
public class AccountService{
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final AppProperties appProperties;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public void modifyCustomer(Account account, MypageDto mypageDto) {
        Account findAccount = accountRepository.findById(account.getId()).get();
        if(findAccount instanceof com.devgyu.banchan.account.customer.Customer){
            com.devgyu.banchan.account.customer.Customer customer = (com.devgyu.banchan.account.customer.Customer) findAccount;
            Address modifyAddress =
                    new Address(mypageDto.getZipcode(), mypageDto.getRoad(), mypageDto.getJibun(), mypageDto.getDetail(), mypageDto.getExtra());
            customer.setAddress(modifyAddress);
            customer.setNickname(mypageDto.getNickname());
            customer.setName(mypageDto.getName());
            customer.setPhone(mypageDto.getPhone());
            if(!mypageDto.getThumbnail().equals("")) {
                customer.setThumbnail(mypageDto.getThumbnail());
            }
        }
    }
    public void modifyPassword(String email, ModifyPasswordDto modifyPasswordDto) throws IllegalAccessException {
        Account findAccount = accountRepository.findByEmail(email);
        if(findAccount == null){
            throw new IllegalAccessException("잘못된 접근 입니다.");
        }
        findAccount.setPassword(passwordEncoder.encode(modifyPasswordDto.getPassword()));
    }

    public String forgotId(ForgotDto forgotDto, BindingResult result) {
        Account findAccount = accountRepository.findByName(forgotDto.getName());
        if(findAccount == null || !findAccount.getPhone().equals(forgotDto.getPhone())){
            result.rejectValue("name", null, "인증에 실패 하였습니다.");
            return null;
        }
        String convertedEmail = emailConvert(findAccount.getEmail());
        return convertedEmail;
    }

    public String emailConvert(String originEmail) {
        String[] split = originEmail.split("@");
        String convertedEmail = split[0].substring(0, 3) + "***********" + "@" + split[1];
        return convertedEmail;
    }

    public void forgotPassword(ForgotDto forgotDto, BindingResult result) {
        Account findAccount = accountRepository.findByEmail(forgotDto.getEmail());
        if(findAccount == null){
            result.rejectValue("email", null, "존재하지 않는 이메일입니다.");
            return;
        }
        String url = appProperties.getHost() + "/forgot/password/" + findAccount.getEmail() + "?token=" + findAccount.getEmailToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(findAccount.getEmail());
        mailMessage.setText(url);
        mailService.send(mailMessage);

        findAccount.setFailCount(0); // 로그인 실패 카운트 0으로 변경
    }
}
