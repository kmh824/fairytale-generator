package com.fairytale.fairytale_generator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@SpringBootTest
public class KomoranServiceTest {

    @Autowired
    private KomoranService komoranService;

    @Test
    public void testExtractKeyNounsWithFullPage() {
        String text = "옛날 옛적에 한 용감한 소년이 살고 있었습니다. " +
                "이 소년은 매일같이 산을 넘어 마을로 여행을 다녔습니다. " +
                "어느 날, 소년은 거대한 용이 산을 넘어 마을을 위협하고 있다는 소식을 들었습니다. " +
                "마을 사람들은 모두 두려워하며 산을 바라보았지만, 소년은 용감하게 용에게 도전하기로 결심했습니다. " +
                "드디어 소년은 용을 만나게 되었고, 그와의 긴 전투 끝에 승리하여 마을을 구해냈습니다.";

        List<String> keywords = komoranService.extractKeyNouns(text);

        // 핵심 키워드들이 추출된 키워드 리스트에 포함되어야 한다.
        assertTrue(keywords.contains("소년"), "키워드에 '소년'이 포함되어야 합니다.");
        assertTrue(keywords.contains("용"), "키워드에 '용'이 포함되어야 합니다.");
        assertTrue(keywords.contains("산"), "키워드에 '산'이 포함되어야 합니다.");
        assertTrue(keywords.contains("마을"), "키워드에 '마을'이 포함되어야 합니다.");

        // 디버깅용 출력
        System.out.println("최종 추출된 키워드: " + keywords);
    }
}
