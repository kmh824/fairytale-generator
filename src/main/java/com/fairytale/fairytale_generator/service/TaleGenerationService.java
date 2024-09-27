// src/main/java/com/fairytale/fairytale_generator/service/TaleGenerationService.java
package com.fairytale.fairytale_generator.service;

import com.fairytale.fairytale_generator.dto.TaleRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaleGenerationService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final WebClient webClient;

    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public TaleGenerationService() {
        this.webClient = WebClient.builder()
                .baseUrl(OPENAI_API_URL)
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public List<String> generateTale(TaleRequestDTO requestDTO) throws Exception {
        // GPT-4-turbo 프롬프트 생성
        String prompt = String.format(
                "다음 조건을 바탕으로 동화를 만들어주세요. 동화의 제목을 먼저 만들어주고, 그 아래에 여러 장으로 구성된 동화 내용을 작성해주세요. 각 장은 최대 5문장으로 구성되어야 합니다.\n" +
                        "주인공: %s\n" +
                        "등장인물: %s\n" +
                        "동화 내용: %s",
                requestDTO.getProtagonist(), requestDTO.getCharacters(), requestDTO.getStoryOutline()
        );

        String requestBody = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"당신은 동화 작가입니다.\"},\n" + // 시스템 메시지를 한국어로 설정
                "    {\"role\": \"user\", \"content\": \"" + prompt + "\"}\n" +
                "  ],\n" +
                "  \"max_tokens\": 500\n" +
                "}";

        // WebClient를 사용하여 API 호출
        Mono<String> responseMono = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);

        // 동기 방식으로 응답 대기 (필요에 따라 비동기 방식으로 처리 가능)
        String responseBody = responseMono.block();

        // JSON 응답 처리
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String taleResponse = jsonNode.path("choices").get(0).path("message").path("content").asText();

        // 동화 내용을 문장 단위로 페이지별로 분리
        List<String> pages = new ArrayList<>();
        String[] sentences = taleResponse.split("\\.");
        StringBuilder pageContent = new StringBuilder();
        int sentenceCount = 0;

        for (String sentence : sentences) {
            if (sentence.trim().isEmpty()) continue;

            if (sentenceCount >= 5) {  // 페이지당 5문장 제한
                pages.add(pageContent.toString().trim());
                pageContent.setLength(0);  // 페이지 내용 초기화
                sentenceCount = 0;
            }
            pageContent.append(sentence).append(". ");
            sentenceCount++;
        }

        // 마지막 페이지 추가
        if (pageContent.length() > 0) {
            pages.add(pageContent.toString().trim());
        }

        return pages;
    }
}
