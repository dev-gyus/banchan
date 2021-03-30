package com.devgyu.banchan.account;

import com.devgyu.banchan.AppProperties;
import com.devgyu.banchan.account.dto.MypageDto;
import com.devgyu.banchan.commons.email.MailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final AppProperties appProperties;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

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
        if(!mypageDto.getThumbnail().equals("")) {
            findAccount.setThumbnail(mypageDto.getThumbnail());
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

    @Transactional(readOnly = true)
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
    }

    /*
    public void saveImageFile(Account targetAccount, MultipartFile thumbnailFile, String path) throws IOException {
        String uploadFolderPrefix = appProperties.getUploadFolderPrefix();
        File folder = new File(uploadFolderPrefix + "/" + path + "/");
        if(!folder.exists()){
            folder.mkdirs();
        }
        String extensionName = StringUtils.getFilenameExtension(thumbnailFile.getOriginalFilename());
        String fileName = UUID.randomUUID() + "_" + LocalDateTime.now() + "." + extensionName;
        File fileProperty = new File(folder, fileName);
        thumbnailFile.transferTo(fileProperty);

        targetAccount.setThumbnail(fileName);
        targetAccount.setOriginThumbnailName(thumbnailFile.getOriginalFilename());
    }
     */
}
