package com.devgyu.banchan.modules.counselor;

import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CounselorService {
    private final CounselorRepository counselorRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveCounselor(){
        String password = "vmffkd495";
        Address address = new Address("12345", "신림로11길", "12345", "12345", "12345");
        Counselor newCounselor = new Counselor("flandre495@gmail.com", "규스상담사", passwordEncoder.encode(password), "규스", "010-4163-8922", Roles.ROLE_COUNSELOR, address);
        counselorRepository.save(newCounselor);
    }
}
