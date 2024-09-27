package com.fairytale.fairytale_generator.service;

import org.springframework.stereotype.Service;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KomoranService {

    private final Komoran komoran;

    public KomoranService() {
        this.komoran = new Komoran(DEFAULT_MODEL.FULL); // FULL 모델을 사용합니다.
    }

    /**
     * 텍스트에서 핵심 키워드를 추출하는 메서드
     *
     * @param text 분석할 텍스트
     * @return 추출된 핵심 키워드 리스트
     */
    public List<String> extractKeyNouns(String text) {
        KomoranResult result = komoran.analyze(text);

        // 모든 명사 추출
        List<String> nouns = result.getNouns();

        // 디버깅용 - 추출된 모든 명사 출력
        System.out.println("추출된 모든 명사: " + nouns);

        // 불용어 리스트 설정 (불필요한 단어 제거)
        Set<String> stopwords = new HashSet<>(Arrays.asList("있다", "것", "이다", "그", "저", "이", "등", "수", "더", "했다", "합니다"));

        // 불용어 제거 및 빈도 계산
        Map<String, Long> frequencyMap = nouns.stream()
                .filter(word -> !stopwords.contains(word)) // 불용어 필터링
                .collect(Collectors.groupingBy(word -> word, Collectors.counting())); // 빈도수 계산

        // 추출된 키워드 확인
        List<String> keyNouns = frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // 빈도수 기준으로 정렬
                .limit(10) // 상위 10개 키워드 추출
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 디버깅용 - 최종 추출된 키워드 출력
        System.out.println("추출된 키워드: " + keyNouns);

        return keyNouns;
    }
}
