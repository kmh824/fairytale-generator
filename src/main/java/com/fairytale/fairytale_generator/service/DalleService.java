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

    private final WebClient webClient;
    private final KomoranService komoranService;
    private final String DALLE_API_URL = "https://api.openai.com/v1/images/generations";

    @Autowired
    public DalleService(KomoranService komoranService) {
        this.komoranService = komoranService;
        this.webClient = WebClient.builder()
                .baseUrl(DALLE_API_URL)
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public List<String> generateIllustrations(String coverPrompt, List<String> pageContents) throws Exception {
        List<String> allImageUrls = new ArrayList<>();

        // 커버 삽화 생성
        String coverImageUrl = generateSingleIllustration(coverPrompt);
        allImageUrls.add(coverImageUrl);

        // 각 페이지의 삽화 생성
        for (String pageContent : pageContents) {
            // KomoranService를 이용하여 키워드 추출
            List<String> keyElements = komoranService.extractKeyNouns(pageContent);
            String prompt = "An illustration showing a scene where " + String.join(", ", keyElements);

            String pageImageUrl = generateSingleIllustration(prompt);
            allImageUrls.add(pageImageUrl);
        }

        return allImageUrls;
    }

    private String generateSingleIllustration(String prompt) throws Exception {
        String requestBody = "{\n" +
                "  \"model\": \"dall-e\",\n" +
                "  \"prompt\": \"" + prompt + "\",\n" +
                "  \"num_images\": 1,\n" +
                "  \"size\": \"512x512\" \n" +
                "}";

        Mono<String> responseMono = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);

        String responseBody = responseMono.block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.path("data").get(0).path("url").asText();
    }
}
