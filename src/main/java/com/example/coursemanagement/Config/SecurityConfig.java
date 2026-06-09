package com.example.coursemanagement.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
// cấu hình bảo mật
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    // bean tạo bộ lọc bảo mật
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // phân quyền truy cập
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tất cả mọi người truy cập
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/manager.css", "/manager.js").permitAll()
                        // Tất cả  phải ĐĂNG NHẬP mới được vào
                        .requestMatchers("/course/**").authenticated()
                        .anyRequest().permitAll()
                )
                // cấu hình trang đăng nhập
                .formLogin(form -> form
                        // Đường dẫn đến trang đăng nhập tự tùy biến
                        .loginPage("/Login")
                        // Đường dẫn xử lý submit data đăng nhập
                        .loginProcessingUrl("/authenticate")
                        // Nếu đăng nhập thành công => trang danh sách khóa học
                        .defaultSuccessUrl("/course", true)
                        .permitAll()
                );

        return http.build();
    }
   // tài khoản hợp lệ để đăng nhập
    @Bean
    public UserDetailsService userDetailsService() {
        // Tạo tạm một tài khoản adim
        UserDetails admin = User.withUsername("admin")
                .password("{noop}123123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}