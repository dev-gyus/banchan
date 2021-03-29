package com.devgyu.banchan.account;

import com.devgyu.banchan.account.dto.MypageDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account findAccount = accountRepository.findByEmail(username);
        if(findAccount == null){
            throw new UsernameNotFoundException("로그인에 실패하였습니다.");
        }
        return new UserAccount(findAccount.getEmail(), findAccount.getPassword(), Arrays.asList(new SimpleGrantedAuthority(findAccount.getRole().toString())), findAccount);
    }

    public void modifyAccount(Account account, MypageDto mypageDto) {
        Account findAccount = accountRepository.findById(account.getId()).get();
        Address modifyAddress =
                new Address(mypageDto.getZipcode(), mypageDto.getRoad(), mypageDto.getJibun(), mypageDto.getDetail(), mypageDto.getExtra());
        findAccount.setAddress(modifyAddress);
        findAccount.setNickname(mypageDto.getNickname());
        findAccount.setName(mypageDto.getName());
        findAccount.setPhone(mypageDto.getPhone());
    }
}
