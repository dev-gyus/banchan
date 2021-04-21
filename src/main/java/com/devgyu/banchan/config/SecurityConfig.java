package com.devgyu.banchan.config;

import com.devgyu.banchan.account.AccountService;
import com.devgyu.banchan.account.LoginService;
import com.devgyu.banchan.config.handler.AccountLoginFailureHandler;
import com.devgyu.banchan.config.handler.AccountLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final LoginService loginService;
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final AccountLoginSuccessHandler accountLoginSuccessHandler;
    private final AccountLoginFailureHandler accountLoginFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(loginService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/", "/login/**", "/login", "/register/**", "/forgot/**").permitAll()
                .mvcMatchers("/mypage/**").hasRole("USER")
                .mvcMatchers("/mystore/**", "/items/**", "/items").hasRole("OWNER")
                .mvcMatchers("/rider/**").hasRole("RIDER")
                .mvcMatchers("/counselor/**").hasRole("COUNSELOR")
                .mvcMatchers("/prepare/**", "/admin/**").hasRole("ADMIN")
                .mvcMatchers("/temp/**").permitAll()
                .anyRequest().authenticated();

        http
                .formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
                .loginPage("/login")
                .successHandler(accountLoginSuccessHandler)
                .failureHandler(accountLoginFailureHandler);

        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");


        http
                .sessionManagement()
                .sessionFixation()
                .changeSessionId()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/")
                .sessionRegistry(sessionRegistry());

        http
                .rememberMe()
                .tokenValiditySeconds(60 * 60 * 24 * 14)
                .userDetailsService(loginService)
                .rememberMeParameter("rememberLogin")
                .rememberMeCookieName("rememberLogin")
                .tokenRepository(jdbcTokenRepository());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .mvcMatchers("/node_modules/**", "/images/**", "/api/**", "/upload/**");
    }

    @Bean
    public PersistentTokenRepository jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
    // 세션 레지스트리 빈을 주입받아 사용하기 위해 설정
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
    // 세션이 만료될시 SpringSecurity가 이를 감지할 수 있도록 리스너 빈을 설정해줌
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher(){
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }
}
