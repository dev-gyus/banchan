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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

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
        modelMapper.map(findAccount.getAddress(), map);
        model.addAttribute(map);
        return "mypage/main";
    }
    @PostMapping("/mypage/modify")
    public String mypage_modify(@CurrentUser Account account, @Valid @ModelAttribute MypageDto mypageDto,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes){
        if(mypageDto.getEmail() != null){
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        if(result.hasErrors()){
            mypageDto.setEmail(account.getEmail());
            return "/mypage/main";
        }

        accountService.modifyAccount(account, mypageDto);

        return "redirect:/mypage";
    }

}
