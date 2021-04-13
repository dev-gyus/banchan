package com.devgyu.banchan.admin;

import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
@Transactional
@RequiredArgsConstructor
public class PrepareAdminController {
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    @GetMapping("/temp/prepare-admin")
    public String prepare_admin() {
        Address newAddress = new Address("", "", "", "", "");
        Admin admin = new Admin("cjsworbehd13@naver.com", "관리자",
                passwordEncoder.encode("vmffkd495"), "관리자", "", Roles.ROLE_ADMIN, newAddress, UUID.randomUUID().toString());
        adminRepository.save(admin);
        return "redirect:/";
    }
}
