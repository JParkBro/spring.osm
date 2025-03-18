package com.example.onlineshoppingmall.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .antMatcher("/admin/**")
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/admin/login", "/admin/loginProc").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/admin/login")
                    .loginProcessingUrl("/admin/loginProc")
                    .defaultSuccessUrl("/admin/users/list", true)
                    .usernameParameter("userId")
                    .passwordParameter("password")
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/admin/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll();

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {

        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setCreateSessionAllowed(true);

        http
                .requestCache(cache -> cache
                        .requestCache(requestCache))
                .antMatcher("/**")
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/auth/login").permitAll()
                    .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                    .anyRequest().permitAll()
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> {
                        // 인증되지 않은 요청 처리 (로그인 페이지로 리다이렉트)
                        response.sendRedirect("/auth/login");
                    })
                    .and()
                .formLogin()
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/auth/loginProc")
                    .successHandler(customAuthenticationSuccessHandler)
                    .usernameParameter("userId")
                    .passwordParameter("password")
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TODO 로그인 실패 핸들러 만들기
}
