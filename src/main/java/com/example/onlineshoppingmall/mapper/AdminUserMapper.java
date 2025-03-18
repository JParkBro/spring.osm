package com.example.onlineshoppingmall.mapper;

import com.example.onlineshoppingmall.domain.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminUserMapper {

    int totalCount(Map<String, Object> params);

    List<UserEntity> findUsersWithPagination(Map<String, Object> params);

    UserEntity findUserById(String userId);

    int insertUser(UserEntity userEntity);

    int updateUser(UserEntity userEntity);
}
