package com.ttasum.memorial.dto.heavenLetter.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

//등록
@Getter
@Setter
public class HeavenLetterRequest {

@NotBlank(message = "필수 입력값이 누락되었습니다.")
private String letterWriter;

private String anonymityFlag;

@NotBlank(message = "필수 입력값이 누락되었습니다.")
private String letterPasscode;

private String donorName;

private Integer donateSeq;

@NotBlank(message = "필수 입력값이 누락되었습니다.")
private String areaCode;

@NotBlank(message = "필수 입력값이 누락되었습니다.")
private String letterTitle;

@NotBlank(message = "필수 입력값이 누락되었습니다.")
private String letterContents;

private String fileName;

private String orgFileName;

private String writerId;
}




