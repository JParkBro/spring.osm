package com.example.onlineshoppingmall.service;

import com.example.onlineshoppingmall.domain.Role;
import com.example.onlineshoppingmall.dto.ProfileDTO;
import com.example.onlineshoppingmall.mapper.AuthMapper;
import com.example.onlineshoppingmall.dto.AuthLoginDTO;
import com.example.onlineshoppingmall.dto.AuthRegisterDTO;
import com.example.onlineshoppingmall.dto.AuthResponseDTO;
import com.example.onlineshoppingmall.domain.UserConverter;
import com.example.onlineshoppingmall.domain.UserEntity;
import com.example.onlineshoppingmall.repository.UserRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;
    private final UserRedisRepository userRedisRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Map<String, Boolean>> checkDuplicate(String userId) {
        Map<String, Boolean> response = new HashMap<>();
        if (authMapper.findByUserId(userId) != null) {
            response.put("isDuplicate", true);
        } else {
            response.put("isDuplicate", false);
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> registerUser(AuthRegisterDTO authRegisterDTO) {
        UserEntity userEntity = UserConverter.toEntity(authRegisterDTO);

        // ID 중복 검사 또는 추가 유효성 검사
        if (authMapper.findByUserId(userEntity.getUserId()) != null) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 암호화
        userEntity.setPassword(passwordEncoder.encode(authRegisterDTO.getPassword()));

        // TODO 수정 필요
        userEntity.setRole(String.valueOf(Role.USER)); // 일반유저 지정
        userEntity.setPostalCode("65527");
        userEntity.setAddress("전남 나주시 문화로 184");
        userEntity.setDetailAddress("스카이센트럴 1228호");
        userEntity.setCreatedUser("JParkBro");

        int result = authMapper.registerUser(userEntity);
        if (result > 0) {
            // Redis 캐시에 저장
            userRedisRepository.save(userEntity);

            return ResponseEntity.ok("Registration successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }
    }

    public void updateProfile(ProfileDTO profileDTO) {
        UserEntity user = authMapper.getUserById(profileDTO.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        if (!passwordEncoder.matches(profileDTO.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("현재 비밀번호가 일치하지 않습니다.");
        } else {
            profileDTO.setCurrentPassword(user.getPassword());
        }

        if (profileDTO.getNewPassword() != null && !profileDTO.getNewPassword().isEmpty()) {
            profileDTO.setNewPassword(passwordEncoder.encode(profileDTO.getNewPassword()));
        }

        int result = authMapper.updateProfile(profileDTO);

        if (result > 0) {
            userRedisRepository.save(user);
        }
    }
}
