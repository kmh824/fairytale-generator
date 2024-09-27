package com.fairytale.fairytale_generator.controller;

import com.fairytale.fairytale_generator.dto.TaleRequestDTO;
import com.fairytale.fairytale_generator.dto.TaleResponseDTO;
import com.fairytale.fairytale_generator.service.TaleGenerationService;
import com.fairytale.fairytale_generator.service.DalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tales")
public class TaleController {

    @Autowired
    private TaleGenerationService taleGenerationService;

    @Autowired
    private DalleService dalleService;

    @PostMapping("/generate")
    public TaleResponseDTO generateTale(@RequestBody TaleRequestDTO requestDTO) {
        try {
            // 1. GPT API를 통해 동화 생성 및 페이지별로 분리
            List<String> talePages = taleGenerationService.generateTale(requestDTO);

            // 첫 번째 페이지의 내용은 제목이므로 분리합니다.
            String title = talePages.remove(0);

            // 2. DALL-E 3 API를 통해 표지 및 페이지별 삽화 생성
            String coverPrompt = "A beautiful cover illustration of a magical fairy tale book with sparkling stars and a castle";
            List<String> illustrationUrls = dalleService.generateIllustrations(coverPrompt, talePages);

            // 3. 동화 제목, 내용과 삽화 URL을 함께 전달
            return new TaleResponseDTO(null, null, title, talePages, illustrationUrls);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaleResponseDTO(null, null, "Error", List.of("Error generating the tale."), List.of());

        }
    }
}
