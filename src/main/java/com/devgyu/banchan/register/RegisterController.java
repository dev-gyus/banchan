package com.devgyu.banchan.register;

import com.devgyu.banchan.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final RegisterService registerService;
    private final RegisterRepository registerRepository;
    @GetMapping({"","/"})
    public String register(@ModelAttribute RegisterDto registerDto){
        return "register/main";
    }
    @PostMapping({"","/"})
    public String register_do(@Valid @ModelAttribute RegisterDto registerDto, BindingResult result, Model model){
        duplicationCheck(registerDto, result);
        if(result.hasErrors()){
            return "register/main";
        }
        Account account = registerService.register(registerDto);
        model.addAttribute("account", account);
        return "register/done";
    }
    @PostMapping("/rider")
    public String register_rider_do(@Valid @ModelAttribute RegisterDto registerDto, BindingResult result, Model model){
        duplicationCheck(registerDto, result);
        if(result.hasErrors()){
            return "register/main";
        }
        Account account = registerService.register(registerDto);
        model.addAttribute("account", account);
        return "register/done";
    }

    private void duplicationCheck(RegisterDto registerDto, BindingResult result) {
        boolean emailResult = registerRepository.existsByEmail(registerDto.getEmail());
        boolean nicknameResult = registerRepository.existsByNickname(registerDto.getNickname());
        if(emailResult){
            result.rejectValue("email", null, "이미 존재하는 이메일 입니다.");
        }else if(nicknameResult){
            result.rejectValue("nickname", null, "이미 존재하는 닉네임 입니다.");
        }
    }
}
