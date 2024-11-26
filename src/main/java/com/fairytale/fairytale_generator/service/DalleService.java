package com.fairytale.fairytale_generator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

@Service
public class DalleService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final WebClient.Builder webClientBuilder;
    private final KomoranService komoranService;
    private final String DALLE_API_URL = "https://api.openai.com/v1/images/generations";

    @Autowired
    public DalleService(WebClient.Builder webClientBuilder, KomoranService komoranService) {
        this.webClientBuilder = webClientBuilder;
        this.komoranService = komoranService;
    }

    // DalleService.java
    public List<String> generateIllustrations(String title, List<String> pageContents) {
        List<String> allImageUrls = new ArrayList<>();

        try {
            // 동화 제목이 유효한지 확인
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("동화의 제목이 유효하지 않습니다.");
            }

            // 동화 제목을 활용한 표지 삽화 생성
            String coverPrompt = title + "라는 동화의 아름다운 표지 삽화를 만들어주세요.";
            System.out.println("표지 삽화 생성 프롬프트: " + coverPrompt);
            String coverImageUrl = generateSingleIllustration(coverPrompt);
            allImageUrls.add(coverImageUrl);

            // 각 페이지별 삽화 생성 (Komoran 적용)
            for (int i = 0; i < pageContents.size(); i++) {
                String pageContent = pageContents.get(i);

                // Komoran을 이용하여 키워드 추출
                List<String> keyElements = komoranService.extractKeyNouns(pageContent);

                String prompt;
                if (keyElements.isEmpty()) {
                    prompt = "다음 동화의 내용을 표현하는 삽화를 그려주세요 " + pageContent.substring(0, Math.min(50, pageContent.length())) + "...";
                } else {
                    prompt = "다음 동화 내용에서 추출한 키워드를 보고 어울리는 동화 삽화를 그려주세요: " + String.join(", ", keyElements);
                }

                System.out.println("Generating illustration for Page " + (i + 1) + " with prompt: " + prompt);

                String pageImageUrl = generateSingleIllustration(prompt);
                allImageUrls.add(pageImageUrl);

                // 삽화 수가 총 장 수 + 1개를 초과하지 않도록 확인
                if (allImageUrls.size() >= pageContents.size() + 1) break;
            }

            System.out.println("Total illustrations generated: " + allImageUrls.size());

        } catch (Exception e) {
            System.err.println("Error generating illustrations: " + e.getMessage());
            throw new RuntimeException("삽화 생성 중 오류가 발생했습니다.", e);
        }

        return allImageUrls;
    }


    private String generateSingleIllustration(String prompt) throws Exception {
        String requestBody = "{\n" +
                "  \"model\": \"dall-e-3\",\n" +
                "  \"prompt\": \"" + prompt + "\",\n" +
                "  \"num_images\": 1,\n" +
                "  \"size\": \"1024x1024\"\n" +
                "}";

        System.out.println("Request Body to DALL-E API: " + requestBody);

        WebClient webClient = webClientBuilder
                .baseUrl(DALLE_API_URL)
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();

        Mono<String> responseMono = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> {
                    System.err.println("Error response from DALL-E API: " + e.getMessage());
                });

        String responseBody = responseMono.block();

        System.out.println("DALL-E API Response: " + responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        if (!jsonNode.has("data") || !jsonNode.path("data").get(0).has("url")) {
            throw new RuntimeException("DALL-E API로부터 올바른 응답을 받지 못했습니다.");
        }

        return jsonNode.path("data").get(0).path("url").asText();
    }
}
