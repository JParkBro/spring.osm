package com.example.onlineshoppingmall.controller;

import com.example.onlineshoppingmall.dto.AuthLoginDTO;
import com.example.onlineshoppingmall.dto.AuthRegisterDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthPageController {

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new AuthRegisterDTO());
        return "pages/auth/register";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new AuthLoginDTO());
        return "pages/auth/login";
    }
}
