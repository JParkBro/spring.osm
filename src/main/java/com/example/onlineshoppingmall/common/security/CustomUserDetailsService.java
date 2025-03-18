package com.example.onlineshoppingmall.common.security;

import com.example.onlineshoppingmall.domain.UserEntity;
import com.example.onlineshoppingmall.mapper.AuthMapper;
import com.example.onlineshoppingmall.repository.UserRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthMapper authMapper;
    private final UserRedisRepository userRedisRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity userEntity = userRedisRepository.findById(userId)
                .orElseGet(() -> {
                    UserEntity entityFromDb = authMapper.loginUser(userId)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

                    userRedisRepository.save(entityFromDb);
                    return entityFromDb;
                });

        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("ADMIN".equals(userEntity.getRole())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new org.springframework.security.core.userdetails.User(
                userEntity.getUserId(),
                userEntity.getPassword(),
                authorities
        );
    }

    public boolean isAdminLogin(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/admin");
    }
}
