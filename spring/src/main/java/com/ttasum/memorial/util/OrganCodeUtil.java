//package com.ttasum.memorial.util;
//
//import com.ttasum.memorial.domain.enums.OrganCode;
//import com.ttasum.memorial.exception.recipientLetter.RecipientOrganNameEmptyException;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//public class OrganCodeUtil {
//
//    // 장기명 → OrganCode enum 객체 찾기
//    public static Optional<OrganCode> fromName(String name) {
//        return Arrays.stream(OrganCode.values())
//                .filter(code -> code.getName().equalsIgnoreCase(name))
//                .findFirst();
//    }
//
//    // 코드값 → OrganCode enum 객체 찾기
//    public static Optional<OrganCode> fromCode(String code) {
//        return Arrays.stream(OrganCode.values())
//                .filter(organCode -> organCode.getCode().equalsIgnoreCase(code))
//                .findFirst();
//    }
//
//    // 장기명 → 코드값 (없으면 ORGAN000)
//    public static String resolveCodeByName(String name) {
//        return fromName(name)
//                .map(OrganCode::getCode)
//                .orElse(OrganCode.DIRECT_INPUT.getCode());
//    }
//
//    // 코드값 → 장기명 (없으면 "직접입력")
//    public static String resolveNameByCode(String code) {
//        return fromCode(code)
//                .map(OrganCode::getName)
//                .orElse(OrganCode.DIRECT_INPUT.getName());
//    }
//
//    // 장기명 → OrganResult (코드 + 기타장기명)
//    public static OrganResult resolveCodeAndEtc(String inputName) {
//        // 1. 입력이 null 또는 공백이면 예외
//        if (inputName == null || inputName.trim().isEmpty()) {
//            throw new RecipientOrganNameEmptyException();
//        }
//
//        // 2. 다중 입력일 경우 → 직접입력 처리
//        if (inputName.contains(",") ||
//            (inputName.contains(" ") && inputName.split("[,\\s]+").length > 1)) {
//            return new OrganResult(OrganCode.DIRECT_INPUT.getCode(), inputName);
//        }
//
//        // 3. enum에 있는 장기면 코드 반환, 아니면 직접입력 처리
//        Optional<OrganCode> matchedCode = fromName(inputName);
//        if (matchedCode.isPresent()) {
//            return new OrganResult(matchedCode.get().getCode(), null);
//        }
//
//        // 4. enum에 없음 → 직접입력 처리
//        return new OrganResult(OrganCode.DIRECT_INPUT.getCode(), inputName);
//    }
//}