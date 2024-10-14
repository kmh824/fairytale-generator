package com.fairytale.fairytale_generator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Illustration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private int pageNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fairy_tale_id")
    private FairyTale fairyTale;

    // 생성일자 및 수정일자
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate = new Date();
}
