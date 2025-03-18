package com.example.onlineshoppingmall.mapper;

import com.example.onlineshoppingmall.domain.Code;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CodeMapper {
    /**
     * 모든 활성화된 코드 조회
     */
    List<Code> selectAllActiveCodes();

    /**
     * ID로 코드 조회
     */
    Code selectCodeById(Long id);

    /**
     * 코드 타입과 키로 코드 조회
     */
    Code selectCodeByTypeAndKey(@Param("codeType") String codeType, @Param("codeKey") String codeKey);

    /**
     * 특정 타입의 모든 코드 조회
     */
    List<Code> selectCodesByType(String codeType);
}
