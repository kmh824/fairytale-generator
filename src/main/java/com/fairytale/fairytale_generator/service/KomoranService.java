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
        this.komoran = new Komoran(DEFAULT_MODEL.FULL);
    }

    public List<String> extractKeyNouns(String text) {
        KomoranResult result = komoran.analyze(text);
        List<String> nouns = result.getNouns();

        Set<String> stopwords = new HashSet<>(Arrays.asList("있다", "것", "이다", "그", "저", "이", "등", "수", "더", "했다", "합니다"));

        Map<String, Long> frequencyMap = nouns.stream()
                .filter(word -> !stopwords.contains(word))
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

        return frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
