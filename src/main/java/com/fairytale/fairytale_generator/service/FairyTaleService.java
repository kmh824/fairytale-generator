package com.fairytale.fairytale_generator.service;

import com.fairytale.fairytale_generator.entity.FairyTale;
import com.fairytale.fairytale_generator.entity.Illustration;
import com.fairytale.fairytale_generator.exception.FairyTaleNotFoundException;
import com.fairytale.fairytale_generator.exception.UnauthorizedAccessException;
import com.fairytale.fairytale_generator.repository.FairyTaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FairyTaleService {

    private final FairyTaleRepository fairyTaleRepository;

    // 생성자 주입
    public FairyTaleService(FairyTaleRepository fairyTaleRepository) {
        this.fairyTaleRepository = fairyTaleRepository;
    }

    // 동화 생성
    public FairyTale saveFairyTale(String title, String content, Long userId, List<String> imageUrls) {
        FairyTale fairyTale = new FairyTale();
        fairyTale.setTitle(title);
        fairyTale.setContent(content);
        fairyTale.setUserId(userId); // 사용자 ID 설정

        for (int i = 0; i < imageUrls.size(); i++) {
            Illustration illustration = new Illustration();
            illustration.setImageUrl(imageUrls.get(i));
            illustration.setPageNumber(i + 1);
            fairyTale.addIllustration(illustration);
        }

        return fairyTaleRepository.save(fairyTale);
    }

    // 사용자 ID로 동화 목록 조회
    public List<FairyTale> getFairyTalesByUserId(Long userId) {
        return fairyTaleRepository.findByUserId(userId);
    }

    // 동화 수정
    public FairyTale updateFairyTale(Long id, Long userId, String title, String content, List<String> imageUrls) {
        FairyTale fairyTale = fairyTaleRepository.findById(id)
                .orElseThrow(() -> new FairyTaleNotFoundException("해당 ID의 동화를 찾을 수 없습니다."));

        // 권한 검증: 현재 사용자 ID와 동화의 userId가 일치하는지 확인
        validateOwnership(fairyTale, userId);

        fairyTale.setTitle(title);
        fairyTale.setContent(content);

        // 기존의 일러스트레이션 목록을 초기화하고 새롭게 추가
        fairyTale.getIllustrations().clear();
        for (int i = 0; i < imageUrls.size(); i++) {
            Illustration illustration = new Illustration();
            illustration.setImageUrl(imageUrls.get(i));
            illustration.setPageNumber(i + 1);
            fairyTale.addIllustration(illustration);
        }

        return fairyTaleRepository.save(fairyTale);
    }

    // 동화 삭제
    public void deleteFairyTale(Long id, Long userId) {
        FairyTale fairyTale = fairyTaleRepository.findById(id)
                .orElseThrow(() -> new FairyTaleNotFoundException("해당 ID의 동화를 찾을 수 없습니다."));

        // 권한 검증: 현재 사용자 ID와 동화의 userId가 일치하는지 확인
        validateOwnership(fairyTale, userId);

        fairyTaleRepository.deleteById(id);
    }

    // 권한 검증 메서드
    private void validateOwnership(FairyTale fairyTale, Long userId) {
        if (!fairyTale.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("동화를 수정 또는 삭제할 권한이 없습니다.");
        }
    }
}
