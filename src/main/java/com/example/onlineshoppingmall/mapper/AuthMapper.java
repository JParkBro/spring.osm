package com.example.onlineshoppingmall.mapper;

import com.example.onlineshoppingmall.domain.UserEntity;
import com.example.onlineshoppingmall.dto.ProfileDTO;
import com.example.onlineshoppingmall.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AuthMapper {

    String findByUserId(String userId);

    int registerUser(UserEntity userEntity);

    Optional<UserEntity> loginUser(String userId);

    UserEntity getUserById(String userId);

    int updateProfile(ProfileDTO profileDTO);
}
