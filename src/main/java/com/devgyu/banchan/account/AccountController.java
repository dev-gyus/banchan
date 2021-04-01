package com.devgyu.banchan.account;

import com.devgyu.banchan.account.dto.ForgotDto;
import com.devgyu.banchan.account.dto.LoginDto;
import com.devgyu.banchan.account.dto.ModifyPasswordDto;
import com.devgyu.banchan.account.dto.MypageDto;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final LoginService loginService;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    // TODO Account 엔티티로 받는거 전부 Customer 엔티티로 받도록 변경할것

    @GetMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto){
        return "login";
    }
    @PostMapping("/login")
    public String login_do(@ModelAttribute LoginDto loginDto, BindingResult result, Model model){
        loginService.loadUserByUsername(loginDto.getEmail());
        return "login";
    }
    @PostMapping("/logout")
    public String logout_do(@ModelAttribute LoginDto loginDto, BindingResult result, Model model){
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
    @GetMapping("/forgot/id")
    public String forgot_id(@ModelAttribute ForgotDto forgotDto){
        return "forgot/id";
    }
    @PostMapping("/forgot/id")
    public String forgot_id_do(@ModelAttribute ForgotDto forgotDto, BindingResult result, Model model){
        if(forgotDto.getName().equals("")){
            result.rejectValue("name", null, "필수 항목 입니다.");
        }else if(forgotDto.getPhone().equals("")){
            result.rejectValue("phone", null, "필수 항목 입니다.");
        }
        String convertedEmail = accountService.forgotId(forgotDto, result);
        if(result.hasErrors()){
            return "forgot/id";
        }
        model.addAttribute("convertedEmail",convertedEmail);
        return "forgot/id-done";
    }
    @GetMapping("/forgot/password")
    public String forgot_password(@ModelAttribute ForgotDto forgotDto){
        return "forgot/password";
    }
    @PostMapping("/forgot/password")
    public String forgot_password_do(@ModelAttribute ForgotDto forgotDto, BindingResult result, Model model){
        if(forgotDto.getEmail().equals("")){
            result.rejectValue("email", null, "필수 항목 입니다.");
        }
        accountService.forgotPassword(forgotDto, result);
        if(result.hasErrors()){
            return "forgot/password";
        }
        String convertedEmail = accountService.emailConvert(forgotDto.getEmail());
        model.addAttribute("convertedEmail", convertedEmail);
        return "forgot/password-done";
    }
    @GetMapping("/forgot/password/{email}")
    public String forgot_password_modify(@PathVariable String email, @RequestParam String token,
                                         @ModelAttribute ModifyPasswordDto modifyPasswordDto, Model model){
        Account findAccount = accountRepository.findByEmail(email);
        if(findAccount == null || !findAccount.getEmailToken().equals(token)){
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        model.addAttribute("email", email);
        return "forgot/password-main";
    }
    @PostMapping("/forgot/password/{email}")
    public String forgot_password_modify_do(@PathVariable String email,
                                            @ModelAttribute ModifyPasswordDto modifyPasswordDto, BindingResult result)
                                            throws IllegalAccessException {
        if(modifyPasswordDto.getPasswordRepeat().equals("")){
            result.rejectValue("passwordRepeat",null,"비밀번호 확인은 필수입니다.");
            return "forgot/password-main";
        }else if(modifyPasswordDto.getPassword().equals("")){
            result.rejectValue("password",null,"변경할 비밀번호를 입력해주세요.");
            return "forgot/password-main";
        }else if(!modifyPasswordDto.getPassword().equals(modifyPasswordDto.getPasswordRepeat())){
            result.rejectValue("passwordRepeat",null,"비밀번호가 일치하지 않습니다.");
            return "forgot/password-main";
        }
        accountService.modifyPassword(email, modifyPasswordDto);
        return "forgot/password-main-done";
    }

    @GetMapping("/mypage")
    public String mypage(@CurrentUser Account customer, Model model) throws IllegalAccessException {
        if(customer instanceof StoreOwner){
            throw new IllegalAccessException("잘못된 요청입니다.");
        }
        Account findCustomer = accountRepository.findByEmail(customer.getEmail());
        MypageDto map = modelMapper.map(findCustomer, MypageDto.class);
        modelMapper.map(findCustomer.getAddress(), map);
        model.addAttribute(map);
        return "mypage/main";
    }
    @PostMapping("/mypage/modify")
    public String mypage_modify(@CurrentUser Account customer, @Valid @ModelAttribute MypageDto mypageDto,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes) throws IllegalAccessException {
        if(mypageDto.getEmail() != null){
            throw new IllegalAccessException("잘못된 접근 입니다.");
        }
        if(result.hasErrors()){
            mypageDto.setEmail(customer.getEmail());
            return "/mypage/main";
        }
        accountService.modifyCustomer(customer, mypageDto);
        return "redirect:/mypage";
    }

    @GetMapping("/mypage/modify-password")
    public String modifyPassword(@ModelAttribute ModifyPasswordDto modifyPasswordDto){
        return "mypage/change-password";
    }
    @PostMapping("/mypage/modify-password")
    public String modifyPassword(@CurrentUser Account customer, @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                                 BindingResult result){
        if(modifyPasswordDto.getPassword().equals("") || !passwordEncoder.matches(modifyPasswordDto.getPassword(), customer.getPassword())){
            result.rejectValue("password", null, "인증에 실패 하였습니다.");
            return "mypage/change-password";
        }
        return "mypage/change-password-main";
    }
    @PostMapping("/mypage/modify-password-main")
    public String modifyPassword_main(@CurrentUser Account customer, @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                                      BindingResult result) throws IllegalAccessException {
        if(modifyPasswordDto.getPasswordRepeat().equals("")){
            result.rejectValue("passwordRepeat",null,"비밀번호 확인은 필수입니다.");
            return "mypage/change-password-main";
        }else if(modifyPasswordDto.getPassword().equals("")){
            result.rejectValue("password",null,"변경할 비밀번호를 입력해주세요.");
            return "mypage/change-password-main";
        }else if(!modifyPasswordDto.getPassword().equals(modifyPasswordDto.getPasswordRepeat())){
            result.rejectValue("passwordRepeat",null,"비밀번호가 일치하지 않습니다.");
            return "mypage/change-password-main";
        }
        accountService.modifyPassword(customer.getEmail(), modifyPasswordDto);
        return "redirect:/mypage";
    }

}
