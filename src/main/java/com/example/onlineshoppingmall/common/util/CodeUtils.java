package com.example.onlineshoppingmall.common.util;

import com.example.onlineshoppingmall.domain.Code;
import com.example.onlineshoppingmall.mapper.CodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CodeUtils {

    private final CodeMapper codeMapper;

    // 코드 캐싱을 위한 Map (성능 향상을 위해)
    private Map<String, Map<String, Code>> codeCache = new HashMap<>();

    /**
     * 애플리케이션 시작 시 코드 데이터를 캐싱
     */
    @PostConstruct
    public void init() {
        refreshCodeCache();
    }

    /**
     * 코드 캐시 새로고침
     */
    public void refreshCodeCache() {
        List<Code> allCodes = codeMapper.selectAllActiveCodes();

        // 코드 타입별로 그룹화
        codeCache = allCodes.stream()
                .collect(Collectors.groupingBy(
                        Code::getCodeType,
                        Collectors.toMap(Code::getCodeKey, code -> code)
                ));
    }

    /**
     * 코드 ID로 코드 객체 조회
     */
    public Optional<Code> getCodeById(Long codeId) {
        return Optional.ofNullable(codeMapper.selectCodeById(codeId));
    }

    /**
     * 코드 타입과 키로 코드 ID 조회
     */
    public Long getCodeId(String codeType, String codeKey) {
        if (codeCache.containsKey(codeType) && codeCache.get(codeType).containsKey(codeKey)) {
            return codeCache.get(codeType).get(codeKey).getId();
        }

        // 캐시에 없을 경우 DB에서 조회
        Code code = codeMapper.selectCodeByTypeAndKey(codeType, codeKey);
        return code != null ? code.getId() : null;
    }

    /**
     * 코드 타입과 키로 코드 값 조회
     */
    public String getCodeValue(String codeType, String codeKey) {
        if (codeCache.containsKey(codeType) && codeCache.get(codeType).containsKey(codeKey)) {
            return codeCache.get(codeType).get(codeKey).getCodeValue();
        }

        // 캐시에 없을 경우 DB에서 조회
        Code code = codeMapper.selectCodeByTypeAndKey(codeType, codeKey);
        return code != null ? code.getCodeValue() : null;
    }

    /**
     * 코드 ID로 코드 값 조회
     */
    public String getCodeValueById(Long codeId) {
        Optional<Code> code = getCodeById(codeId);
        return code.map(Code::getCodeValue).orElse(null);
    }

    /**
     * 특정 코드 타입의 모든 코드 조회
     */
    public List<Code> getCodesByType(String codeType) {
        if (codeCache.containsKey(codeType)) {
            return new ArrayList<>(codeCache.get(codeType).values());
        }

        // 캐시에 없을 경우 DB에서 조회
        return codeMapper.selectCodesByType(codeType);
    }

    /**
     * 주문 상태 코드 ID 가져오기 (편의 메서드)
     */
    public Long getOrderStatusCodeId(String statusKey) {
        return getCodeId("ORDER_STATUS", statusKey);
    }

    /**
     * 주문 상태 값 가져오기 (편의 메서드)
     */
    public String getOrderStatusValue(String statusKey) {
        return getCodeValue("ORDER_STATUS", statusKey);
    }

    /**
     * 주문 상태 ID로 상태 값 가져오기 (편의 메서드)
     */
    public String getOrderStatusValueById(Long codeId) {
        return getCodeValueById(codeId);
    }
}