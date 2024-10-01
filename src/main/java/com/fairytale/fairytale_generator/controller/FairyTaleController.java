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

    // 동화 생성
    @PostMapping("/create")
    public TaleResponseDTO createFairyTale(Authentication authentication, @RequestBody TaleRequestDTO taleRequestDTO) {
        Long userId = (Long) authentication.getPrincipal();
        return fairyTaleService.saveFairyTale(taleRequestDTO.getTitle(), taleRequestDTO.getContent(), userId, taleRequestDTO.getImageUrls());
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
        return fairyTaleService.updateFairyTale(id, userId, taleRequestDTO.getTitle(), taleRequestDTO.getContent(), taleRequestDTO.getImageUrls());
    }

    // 동화 삭제
    @DeleteMapping("/delete/{id}")
    public String deleteFairyTale(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        fairyTaleService.deleteFairyTale(id, userId);
        return "동화가 삭제되었습니다.";
    }
}
