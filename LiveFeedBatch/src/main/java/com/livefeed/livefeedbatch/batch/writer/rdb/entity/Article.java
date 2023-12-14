package com.livefeed.livefeedbatch.batch.writer.rdb.entity;

import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
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

    @Builder
    private Article(PressCompany pressCompany, Category category, String title, String originArticleUrl, String contentHeader, String contentBody, String journalistName, String publicationTime) {
        this.pressCompany = pressCompany;
        this.category = category;
        this.title = title;
        this.originArticleUrl = originArticleUrl;
        this.contentHeader = contentHeader;
        this.contentBody = contentBody;
        this.journalistName = journalistName;
        this.publicationTime = publicationTime;
    }

    public static Article from(PressCompany pressCompany, Category category, ParseResultDto value) {
        return Article.builder()
                .pressCompany(pressCompany)
                .category(category)
                .title(value.articleTitle())
                .originArticleUrl(value.originArticleUrl())
                .contentHeader(value.headerHtml())
                .contentBody(value.bodyHtml())
                .journalistName(value.journalistName())
                .publicationTime(value.publicationTime())
                .build();
    }
}
