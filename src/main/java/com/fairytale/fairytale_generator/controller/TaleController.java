package com.fairytale.fairytale_generator.controller;

import com.fairytale.fairytale_generator.dto.TaleGenerationRequestDTO;
import com.fairytale.fairytale_generator.dto.TaleResponseDTO;
import com.fairytale.fairytale_generator.service.TaleGenerationService;
import com.fairytale.fairytale_generator.service.DalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tales")
public class TaleController {

    @Autowired
    private TaleGenerationService taleGenerationService;

    @Autowired
    private DalleService dalleService;

    @PostMapping("/generate")
    public TaleResponseDTO generateTale(@RequestBody TaleGenerationRequestDTO requestDTO) {
        try {
            // 1. GPT API를 통해 동화 생성 및 페이지별로 분리
            Map<String, Object> taleResult = taleGenerationService.generateTale(requestDTO);

            if (taleResult == null || taleResult.isEmpty()) {
                throw new RuntimeException("동화 생성 중 오류가 발생했습니다.");
            }

            String title = (String) taleResult.get("title");
            List<String> talePages = (List<String>) taleResult.get("pages");

            // 2. DALL-E 3 API를 통해 표지 및 페이지별 삽화 생성
            List<String> illustrationUrls = dalleService.generateIllustrations(title, talePages);

            // 3. 동화 제목, 내용과 삽화 URL을 함께 전달
            return new TaleResponseDTO(null, null, title, talePages, illustrationUrls);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaleResponseDTO(null, null, "Error", List.of("Error generating the tale: " + e.getMessage()), List.of());
        }
    }
}
