package com.fairytale.fairytale_generator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class FairyTale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @OneToMany(mappedBy = "fairyTale", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Illustration> illustrations = new ArrayList<>();

    // User ID를 저장하는 필드 추가
    private Long userId;  // 해당 동화를 생성한 사용자의 ID

    // 생성일자 및 수정일자
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate = new Date();

    // 편의 메서드
    public void addIllustration(Illustration illustration) {
        illustrations.add(illustration);
        illustration.setFairyTale(this);
    }
}
