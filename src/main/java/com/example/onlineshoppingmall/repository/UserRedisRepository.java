package com.example.onlineshoppingmall.repository;

import com.example.onlineshoppingmall.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<UserEntity, String> {
}
