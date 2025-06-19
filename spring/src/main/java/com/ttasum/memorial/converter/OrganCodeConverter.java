//package com.ttasum.memorial.converter;
//
//import com.ttasum.memorial.domain.enums.OrganCode;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//
//@Converter(autoApply = true)
//public class OrganCodeConverter implements AttributeConverter<OrganCode, String> {
//
//    //Entity -> DB 저장
//    @Override
//    public String convertToDatabaseColumn(OrganCode organCode) {
//        return organCode != null ? organCode.getCode() : null;
//    }
//
//    //DB-> Entity  조회
//    @Override
//    public OrganCode convertToEntityAttribute(String code) {
//        if (code == null || code.isBlank()) return null;
//        return OrganCode.fromCode(code);
//    }
//}
