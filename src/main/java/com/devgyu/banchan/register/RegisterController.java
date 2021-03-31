package com.devgyu.banchan.register;

import com.devgyu.banchan.register.dto.OwnerRegisterDto;
import com.devgyu.banchan.register.dto.RegisterDto;
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
    public String register_main(@ModelAttribute RegisterDto registerDto){
        return "register/main";
    }

    @GetMapping("/user")
    public String register_user(@ModelAttribute RegisterDto registerDto){
        return "register/user";
    }
    @PostMapping("/user")
    public String user_register_do(@Valid @ModelAttribute RegisterDto registerDto, BindingResult result, Model model){
        duplicationCheck(registerDto, result);
        if(result.hasErrors()){
            return "register/user";
        }
        registerService.register(registerDto);
        return "register/done";
    }

    @GetMapping("/owner")
    public String register_owner(@ModelAttribute OwnerRegisterDto ownerRegisterDto){
        return "register/owner";
    }

    @PostMapping("/owner")
    public String register_owner_do(@Valid @ModelAttribute OwnerRegisterDto ownerRegisterDto, BindingResult result, Model model){
        duplicationCheck(ownerRegisterDto, result);
        if(result.hasErrors()){
            return "register/owner";
        }
        registerService.ownerRegister(ownerRegisterDto);
        return "register/done";
    }


//    @PostMapping("/rider")
//    public String register_rider_do(@Valid @ModelAttribute RegisterDto registerDto, BindingResult result, Model model){
//        duplicationCheck(registerDto, result);
//        if(result.hasErrors()){
//            return "register/main";
//        }
//        Account account = registerService.register(registerDto);
//        model.addAttribute("account", account);
//        return "register/done";
//    }

    private void duplicationCheck(RegisterDto registerDto, BindingResult result) {
        boolean nicknameResult = registerRepository.existsByNickname(registerDto.getNickname());
        if(registerRepository.existsByEmail(registerDto.getEmail())){
            result.rejectValue("email", null, "이미 존재하는 이메일 입니다.");
        }else if(nicknameResult){
            result.rejectValue("nickname", null, "이미 존재하는 닉네임 입니다.");
        }
    }
    private void duplicationCheck(OwnerRegisterDto ownerRegisterDto, BindingResult result) {
        boolean emailResult = registerRepository.existsByEmail(ownerRegisterDto.getEmail());
        if(emailResult){
            result.rejectValue("email", null, "이미 존재하는 이메일 입니다.");
        }
    }
}
