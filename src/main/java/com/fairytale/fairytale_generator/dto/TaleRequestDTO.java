package com.fairytale.fairytale_generator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
//dd
@Getter
@Setter
public class TaleRequestDTO {

    private String title;
    private List<String> content; // 수정: content를 List<String>으로 변경
    private List<String> imageUrls;

    // 기본 생성자
    public TaleRequestDTO() {}

}
