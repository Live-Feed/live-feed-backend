package com.livefeed.livefeedparser.parser.dto;

public record HeaderDto(
        String outerHtml,
        String articleTitle,
        String createdAt,
        String publicationTime,
        String originArticleUrl,
        String journalistName
) {

    public static HeaderDto of(String outerHtml, String articleTitle,
                               String createdAt, String publicationTime,
                               String originArticleUrl, String journalistName) {
        return new HeaderDto(outerHtml, articleTitle, createdAt,
                publicationTime, originArticleUrl, journalistName);
    }
}
