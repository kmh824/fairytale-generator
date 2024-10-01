package com.fairytale.fairytale_generator.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
// TaleGenerationRequestDTO.java
@Getter
@Setter
public class TaleGenerationRequestDTO {
    private Long userId;
    private String protagonist;
    private String characters;
    private String storyOutline;
}

