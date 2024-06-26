package com.livefeed.livefeedviewsbatch.batch.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "press_company_id")
    private PressCompany pressCompany;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;

    private String originArticleUrl;

    private String contentHeader;

    private String contentBody;

    private String journalistName;

    private String publicationTime;

    private LocalDateTime createdAt;

    private int views;

    @Builder
    private Article(PressCompany pressCompany, Category category, String title, String originArticleUrl, String contentHeader, String contentBody, String journalistName, String publicationTime, int views) {
        this.pressCompany = pressCompany;
        this.category = category;
        this.title = title;
        this.originArticleUrl = originArticleUrl;
        this.contentHeader = contentHeader;
        this.contentBody = contentBody;
        this.journalistName = journalistName;
        this.publicationTime = publicationTime;
        this.views = views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
