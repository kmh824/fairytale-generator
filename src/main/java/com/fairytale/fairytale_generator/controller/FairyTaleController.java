package com.fairytale.fairytale_generator.controller;

import com.fairytale.fairytale_generator.entity.FairyTale;
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
    public FairyTale createFairyTale(Authentication authentication, @RequestParam String title,
                                     @RequestParam String content, @RequestBody List<String> imageUrls) {
        Long userId = (Long) authentication.getPrincipal();
        return fairyTaleService.saveFairyTale(title, content, userId, imageUrls);
    }

    // 사용자 ID로 동화 목록 조회
    @GetMapping("/user")
    public List<FairyTale> getFairyTalesByUserId(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return fairyTaleService.getFairyTalesByUserId(userId);
    }

    // 동화 수정
    @PutMapping("/update/{id}")
    public FairyTale updateFairyTale(@PathVariable Long id, Authentication authentication,
                                     @RequestParam String title, @RequestParam String content,
                                     @RequestBody List<String> imageUrls) {
        Long userId = (Long) authentication.getPrincipal();
        return fairyTaleService.updateFairyTale(id, userId, title, content, imageUrls);
    }

    // 동화 삭제
    @DeleteMapping("/delete/{id}")
    public String deleteFairyTale(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        fairyTaleService.deleteFairyTale(id, userId);
        return "동화가 삭제되었습니다.";
    }
}
