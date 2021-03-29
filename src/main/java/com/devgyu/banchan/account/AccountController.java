package com.devgyu.banchan.account;

import com.devgyu.banchan.account.dto.MypageDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto){
        return "login";
    }
    @PostMapping("/login")
    public String login_do(@ModelAttribute LoginDto loginDto, BindingResult result, Model model){
        accountService.loadUserByUsername(loginDto.getEmail());
        return "login";
    }
    @PostMapping("/logout")
    public String logout_do(@ModelAttribute LoginDto loginDto, BindingResult result, Model model){
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(@CurrentUser Account account, Model model){
        Account findAccount = accountRepository.findByEmail(account.getEmail());
        MypageDto map = modelMapper.map(findAccount, MypageDto.class);
        model.addAttribute(map);
        return "mypage/main";
    }

}
