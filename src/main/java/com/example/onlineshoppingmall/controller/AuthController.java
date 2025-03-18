package com.example.onlineshoppingmall.controller;

import com.example.onlineshoppingmall.dto.AuthRegisterDTO;
import com.example.onlineshoppingmall.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/checkDuplicate")
    public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestParam String userId) {
        return authService.checkDuplicate(userId);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AuthRegisterDTO authRegisterDTO) {
        return authService.registerUser(authRegisterDTO);
    }

}
