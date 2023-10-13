package com.livefeed.livefeedsaver.opensearch.entity;

import com.livefeed.livefeedsaver.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedsaver.kafka.consumer.dto.ConsumerValueDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "articles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String type;

    @Field(type = FieldType.Text)
    private String articleTitle;

    @Field(type = FieldType.Text)
    private String publicationTime;

    @Field(type = FieldType.Text)
    private String pressCompanyName;

    @Field(type = FieldType.Text)
    private String journalistName;

    @Field(type = FieldType.Text)
    private String journalistEmail;

    @Field(type = FieldType.Text)
    private String originArticleUrl;

    @Field(type = FieldType.Text)
    private String headerHtml;

    @Field(type = FieldType.Text)
    private String bodyHtml;

    @Builder
    private Article(String type, String articleTitle, String publicationTime, String pressCompanyName, String journalistName, String journalistEmail, String originArticleUrl, String headerHtml, String bodyHtml) {
        this.type = type;
        this.articleTitle = articleTitle;
        this.publicationTime = publicationTime;
        this.pressCompanyName = pressCompanyName;
        this.journalistName = journalistName;
        this.journalistEmail = journalistEmail;
        this.originArticleUrl = originArticleUrl;
        this.headerHtml = headerHtml;
        this.bodyHtml = bodyHtml;
    }

    public static Article from(ConsumerKeyDto key, ConsumerValueDto value) {
        return Article.builder()
                .type(key.theme().name())
                .articleTitle(value.articleTitle())
                .publicationTime(value.publicationTime())
                .pressCompanyName(value.pressCompanyName())
                .journalistName(value.journalistName())
                .journalistEmail(value.journalistEmail())
                .originArticleUrl(value.originArticleUrl())
                .headerHtml(value.headerHtml())
                .bodyHtml(value.bodyHtml())
                .build();
    }
}
