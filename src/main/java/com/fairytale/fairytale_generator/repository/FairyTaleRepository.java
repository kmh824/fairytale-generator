package com.fairytale.fairytale_generator.repository;

import com.fairytale.fairytale_generator.entity.FairyTale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FairyTaleRepository extends JpaRepository<FairyTale, Long> {
    // 사용자 ID로 동화 목록 조회하는 메서드 추가
    List<FairyTale> findByUserId(Long userId);
}
