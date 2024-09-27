package com.fairytale.fairytale_generator.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TaleResponseDTO {
    private Long id;                      // 동화의 ID
    private Long userId;                  // 동화를 생성한 사용자 ID
    private String title;                 // 동화 제목
    private List<String> talePages;       // 동화의 각 페이지 내용
    private List<String> illustrationUrls; // 각 페이지별 삽화 URL 및 표지

    public TaleResponseDTO(Long id, Long userId, String title, List<String> talePages, List<String> illustrationUrls) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.talePages = talePages;
        this.illustrationUrls = illustrationUrls;
    }
}
