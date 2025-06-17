package com.ttasum.memorial.dto.memorial.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

// 기증자 추모관 목록 조회 시 반환용 DTO
@Getter
@AllArgsConstructor
public class MemorialResponseDto {

    private Integer donateSeq;
    private String donorName;
    private String genderFlag;
    private Integer donateAge;
    private String donateDate;
    private Long commentCount;
    private LocalDate donorBirthDate;
}
