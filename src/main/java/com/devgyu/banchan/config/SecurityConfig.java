package com.devgyu.banchan.config;

import com.devgyu.banchan.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AccountService accountService;
    private final DataSource dataSource;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(accountService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/", "/login/**", "/login", "/register/**").permitAll()
                .anyRequest().authenticated();

        http
                .formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
                .loginPage("/login");

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
                .expiredUrl("/");

        http
                .rememberMe()
                .tokenValiditySeconds(60 * 60 * 24 * 14)
                .userDetailsService(accountService)
                .rememberMeParameter("rememberLogin")
                .rememberMeCookieName("rememberLogin")
                .tokenRepository(jdbcTokenRepository());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .mvcMatchers("/node_modules/**", "/images/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public PersistentTokenRepository jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
