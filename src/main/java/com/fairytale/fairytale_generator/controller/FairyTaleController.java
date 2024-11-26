package com.fairytale.fairytale_generator.controller;

import com.fairytale.fairytale_generator.dto.TaleRequestDTO;
import com.fairytale.fairytale_generator.dto.TaleResponseDTO;
import com.fairytale.fairytale_generator.service.FairyTaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fairy-tales")
public class FairyTaleController {

    @Autowired
    private FairyTaleService fairyTaleService;

    // 동화 저장
    @PostMapping("/create")
    public TaleResponseDTO createFairyTale(Authentication authentication, @RequestBody TaleRequestDTO taleRequestDTO) {
        // Authentication에서 사용자 ID 가져오기
        Long userId = (Long) authentication.getPrincipal();
        // content를 List<String> -> String으로 변환하여 저장
        String content = String.join("\n", taleRequestDTO.getContent());
        return fairyTaleService.saveFairyTale(taleRequestDTO.getTitle(), content, userId, taleRequestDTO.getImageUrls());
    }

    // 사용자 ID로 동화 목록 조회
    @GetMapping("/user")
    public List<TaleResponseDTO> getFairyTalesByUserId(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return fairyTaleService.getFairyTalesByUserId(userId);
    }

    // 동화 수정
    @PutMapping("/update/{id}")
    public TaleResponseDTO updateFairyTale(@PathVariable Long id, Authentication authentication,
                                           @RequestBody TaleRequestDTO taleRequestDTO) {
        Long userId = (Long) authentication.getPrincipal();
        String content = String.join("\n", taleRequestDTO.getContent());
        return fairyTaleService.updateFairyTale(id, userId, taleRequestDTO.getTitle(), content, taleRequestDTO.getImageUrls());
    }

    // 동화 삭제
    @DeleteMapping("/delete/{id}")
    public String deleteFairyTale(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        fairyTaleService.deleteFairyTale(id, userId);
        return "동화가 삭제되었습니다.";
    }
}
