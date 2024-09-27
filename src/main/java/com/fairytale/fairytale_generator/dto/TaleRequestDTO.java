package com.fairytale.fairytale_generator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaleRequestDTO {
    private Long userId;          // 사용자 ID
    private String protagonist;   // 주인공
    private String characters;    // 등장인물
    private String storyOutline;  // 동화 줄거리

    // 기본 생성자
    public TaleRequestDTO() {}
}
