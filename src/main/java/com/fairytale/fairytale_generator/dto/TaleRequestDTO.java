package com.fairytale.fairytale_generator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaleRequestDTO {

    private String title;
    private String content;
    private List<String> imageUrls;
    // 기본 생성자
    public TaleRequestDTO() {}
}
