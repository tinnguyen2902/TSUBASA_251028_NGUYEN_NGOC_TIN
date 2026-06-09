package com.example.coursemanagement.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// điều hươướng người dùng sang trang đăng nhập
// controller => đánh dấu  đây là nơi tiếp nhận thông tin đăng nhập vào
@Controller
public class LoginController {
    @GetMapping("/Login")
    public String showLoginPage() {
        return "Login";
    }
}