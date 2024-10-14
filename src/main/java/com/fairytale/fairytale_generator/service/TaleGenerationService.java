package com.fairytale.fairytale_generator.service;

import com.fairytale.fairytale_generator.dto.TaleGenerationRequestDTO;
import com.fairytale.fairytale_generator.exception.JsonParsingException;
import com.fairytale.fairytale_generator.exception.OpenAiApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaleGenerationService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    public TaleGenerationService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder
                .baseUrl(OPENAI_API_URL)
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public Map<String, Object> generateTale(TaleGenerationRequestDTO requestDTO) throws Exception {
        String prompt = String.format(
                "다음 조건을 바탕으로 동화를 만들어주세요. 동화의 제목을 먼저 만들어주고, 그 아래에 여러 장으로 구성된 동화 내용을 작성해주세요. 각 장은 최대 5문장으로 구성되며, 한 장의 내용을 작성할 때마다 빈 줄 두 개로 구분하여 작성해주세요. 이때 제목은 '제목:'으로 시작하여 작성해주시고, 동화 내용은 '장 1:'과 같은 레이블을 포함하지 말고 내용만 작성해주시고, 문장도 친근하고 부드러운 '~했습니다' 형태로 작성해주세요.\n" +
                        "주인공: %s\n" +
                        "등장인물: %s\n" +
                        "동화 내용: %s",
                requestDTO.getProtagonist(), requestDTO.getCharacters(), requestDTO.getStoryOutline()
        );

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "당신은 동화 작가입니다.");
        messages.add(systemMessage);

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);

        requestBodyMap.put("messages", messages);
        requestBodyMap.put("max_tokens", 500);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        System.out.println("Request to OpenAI API: " + requestBody);

        int retryCount = 0;
        int maxRetries = 8;
        long waitTime = 2000;

        while (retryCount < maxRetries) {
            try {
                Mono<String> responseMono = webClient.post()
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToMono(String.class);

                String responseBody = responseMono.block();

                System.out.println("OpenAI API Response: " + responseBody);

                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String taleResponse = jsonNode.path("choices").get(0).path("message").path("content").asText();

                // 동화 제목과 내용을 저장할 리스트와 변수 초기화
                List<String> pages = new ArrayList<>();
                String title = "";

                // GPT 응답을 두 개의 빈 줄을 기준으로 분리
                String[] sections = taleResponse.split("\n\n");

                // 첫 번째 section을 제목으로 설정하고, "제목:"이라는 레이블이 있는지 확인 후 제거
                if (sections.length > 0 && sections[0].trim().startsWith("제목:")) {
                    title = sections[0].substring("제목:".length()).trim();
                } else {
                    throw new OpenAiApiException("동화의 제목을 추출하지 못했습니다.");
                }

                // 나머지 sections는 페이지 내용으로 추가
                for (int i = 1; i < sections.length; i++) {
                    if (!sections[i].trim().isEmpty()) { // 빈 내용이 아닌 경우에만 추가
                        pages.add(sections[i].trim());
                    }
                }

                // 제목이 비어있는 경우 예외 처리 추가
                if (title.isEmpty()) {
                    throw new OpenAiApiException("동화의 제목을 추출하지 못했습니다.");
                }

                // 결과 확인 및 반환
                System.out.println("Extracted title: " + title);
                System.out.println("Generated pages: " + pages);

                Map<String, Object> result = new HashMap<>();
                result.put("title", title);
                result.put("pages", pages);

                return result;


            } catch (WebClientResponseException ex) {
                if (ex.getStatusCode().value() == 429) {
                    retryCount++;
                    System.err.println("429 Too Many Requests 발생 - " + retryCount + "회 재시도 중...");
                    Thread.sleep(waitTime);
                } else {
                    throw new OpenAiApiException("OpenAI API 호출 중 오류가 발생했습니다.", ex);
                }
            } catch (JsonProcessingException e) {
                throw new JsonParsingException("JSON 파싱 중 오류가 발생했습니다.", e);
            }
        }

        throw new OpenAiApiException("OpenAI API 호출이 허용된 재시도 횟수를 초과했습니다.");
    }
}
