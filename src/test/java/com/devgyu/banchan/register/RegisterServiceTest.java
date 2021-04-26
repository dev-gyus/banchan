package com.devgyu.banchan.register;

import com.devgyu.banchan.register.dto.RegisterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
//@AutoConfigureMockMvc
class RegisterServiceTest {
//    @Autowired
//    private RegisterService registerService;
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void 회원가입_Get() throws Exception{
//        mockMvc.perform(get("/register")
//                        .requestAttr("registerDto", new RegisterDto()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(view().name("register/main"));
//    }
//    @Test
//    @Transactional
//    public void 회원가입_Post_성공() throws Exception{
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd13@gmail.com")
//                .param("password", "12345").param("nickname", "규스스")
//                .param("name", "이규형").param("phone", "01012341234")
//                .param("zipcode", "12345").param("road", "신림로1111")
//                .param("jibun", "지번주소").param("detail","상세주소")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(view().name("register/done"));
//    }
//    @Test
//    @Transactional
//    public void 회원가입_Post_유효성_실패() throws Exception{
//        // 이메일
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd13gmail.com")
//                .param("password", "12345").param("nickname", "규스스")
//                .param("name", "이규형").param("phone", "01012341234")
//                .param("zipcode", "12345").param("road", "신림로1111")
//                .param("jibun", "지번주소").param("detail","상세주소")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().hasErrors())
//                .andExpect(view().name("register/main"));
//        // 패스워드
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd13@gmail.com")
//                .param("password", "123").param("nickname", "규스스")
//                .param("name", "이규형").param("phone", "01012341234")
//                .param("zipcode", "12345").param("road", "신림로1111")
//                .param("jibun", "지번주소").param("detail","상세주소")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().hasErrors())
//                .andExpect(view().name("register/main"));
//        // 닉네임
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd13@gmail.com")
//                .param("password", "12345").param("nickname", "규")
//                .param("name", "이규형").param("phone", "01012341234")
//                .param("zipcode", "12345").param("road", "신림로1111")
//                .param("jibun", "지번주소").param("detail","상세주소")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().hasErrors())
//                .andExpect(view().name("register/main"));
//        // 이름
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd13@gmail.com")
//                .param("password", "12345").param("nickname", "규스스")
//                .param("name", "이").param("phone", "01012341234")
//                .param("zipcode", "12345").param("road", "신림로1111")
//                .param("jibun", "지번주소").param("detail","상세주소")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().hasErrors())
//                .andExpect(view().name("register/main"));
//        // 휴대폰번호
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd13@gmail.com")
//                .param("password", "12345").param("nickname", "규스스")
//                .param("name", "이규형").param("phone", "010123412")
//                .param("zipcode", "12345").param("road", "신림로1111")
//                .param("jibun", "지번주소").param("detail","상세주소")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().hasErrors())
//                .andExpect(view().name("register/main"));
//        // 우편번호쪽
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd13@gmail.com")
//                .param("password", "12345").param("nickname", "규스스")
//                .param("name", "이규형").param("phone", "010123412")
//                .param("zipcode", "").param("road", "")
//                .param("jibun", "").param("detail","")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().hasErrors())
//                .andExpect(view().name("register/main"));
//    }
//    @Test
//    @Transactional
//    public void 회원가입_Post_이메일_중복검사_실패() throws Exception{
//        // 이메일
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd13@gmail.com")
//                .param("password", "12345").param("nickname", "규스스")
//                .param("name", "이규형").param("phone", "01012341234")
//                .param("zipcode", "12345").param("road", "신림로1111")
//                .param("jibun", "지번주소").param("detail","상세주소")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(view().name("register/done"));
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd13@gmail.com")
//                .param("password", "12345").param("nickname", "규스")
//                .param("name", "이규형").param("phone", "01012341234")
//                .param("zipcode", "12345").param("road", "신림로1111")
//                .param("jibun", "지번주소").param("detail","상세주소")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().hasErrors())
//                .andExpect(view().name("register/main"));
//    }
//
//    @Test
//    @Transactional
//    public void 회원가입_Post_닉네임_중복검사_실패() throws Exception{
//        // 이메일
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd15@gmail.com")
//                .param("password", "12345").param("nickname", "규스스")
//                .param("name", "이규형").param("phone", "01012341234")
//                .param("zipcode", "12345").param("road", "신림로1111")
//                .param("jibun", "지번주소").param("detail","상세주소")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(view().name("register/done"));
//        mockMvc.perform(post("/register")
//                .param("email", "cjsworbehd13@gmail.com")
//                .param("password", "12345").param("nickname", "규스스")
//                .param("name", "이규형").param("phone", "01012341234")
//                .param("zipcode", "12345").param("road", "신림로1111")
//                .param("jibun", "지번주소").param("detail","상세주소")
//                .param("extra","추가주소").with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().hasErrors())
//                .andExpect(view().name("register/main"));
//    }
}