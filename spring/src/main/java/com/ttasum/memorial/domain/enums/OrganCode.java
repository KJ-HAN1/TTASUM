package com.ttasum.memorial.domain.enums;

import java.util.Optional;

public enum OrganCode {

    ORGAN_001("ORGAN001", "신장"),
    ORGAN_002("ORGAN002", "간장"),
    ORGAN_003("ORGAN003", "췌장"),
    ORGAN_004("ORGAN004", "심장"),
    ORGAN_005("ORGAN005", "폐"),
    ORGAN_006("ORGAN006", "췌도"),
    ORGAN_007("ORGAN007", "소장"),
    ORGAN_008("ORGAN008", "대장"),
    ORGAN_009("ORGAN009", "위장"),
    ORGAN_010("ORGAN010", "십이지장"),
    ORGAN_011("ORGAN011", "비장"),
    ORGAN_012("ORGAN012", "손, 팔"),
    ORGAN_013("ORGAN013", "안구"),
    ORGAN_014("ORGAN014", "인체조직"),
    DIRECT_INPUT("ORGAN000", "직접입력");

    private final String code;
    private final String name;

    OrganCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
//    //코드값에서 enum 변환
//    public static OrganCode fromCode(String code) {
//        for (OrganCode organCode : values()) {
//            if (organCode.code.equalsIgnoreCase(code)) {
//                return organCode;
//            }
//        }
//        throw new IllegalArgumentException("존재하지 않는 OrganCode 코드: " + code);
//    }
//    //장기명에서 enum 변환
//    public static OrganCode fromName(String name) {
//        for (OrganCode organCode : values()) {
//            if (organCode.name.equalsIgnoreCase(name)) {
//                return organCode;
//            }
//        }
//        throw new IllegalArgumentException("존재하지 않는 OrganCode 장기명: " + name);
//    }
    //검색용 변환
    public static Optional<OrganCode> findByName(String name) {
        for (OrganCode organCode : values()) {
            if (organCode.name.equalsIgnoreCase(name)) {
                return Optional.of(organCode);
            }
        }
        return Optional.empty();

    }
}