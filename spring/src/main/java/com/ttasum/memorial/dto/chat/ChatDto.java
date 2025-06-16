package com.ttasum.memorial.dto.chat;

import lombok.*;

/**
 * 사용자 입력과 관련된 데이터를 담는 DTO
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private String question;
}