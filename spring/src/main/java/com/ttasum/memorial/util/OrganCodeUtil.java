package com.ttasum.memorial.util;

import com.ttasum.memorial.domain.enums.OrganCode;

import java.util.Arrays;
import java.util.Optional;

public class OrganCodeUtil {

    //장기명 -> OrganCode 찾기
    public static Optional<OrganCode> fromName(String name) {
        return Arrays.stream(OrganCode.values())
                .filter(code -> code.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    // OrganCode -> OrganCode 찾기
    public static Optional<OrganCode> fromCode(String code) {
        return Arrays.stream(OrganCode.values())
                .filter(organCode -> organCode.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    //장기명으로 OrganCode 가져오기 (없으면 ORGAN000 반환)
    public static String resolveCodeByName(String name) {
        return fromName(name)
                .map(OrganCode::getCode)
                .orElse(OrganCode.DIRECT_INPUT.getCode());
    }

    // OrganCode 코드값으로 장기명 가져오기 (없으면 "직접입력")
    public static String resolveNameByCode(String code) {
        return fromCode(code)
                .map(OrganCode::getName)
                .orElse(OrganCode.DIRECT_INPUT.getName());
    }
}
