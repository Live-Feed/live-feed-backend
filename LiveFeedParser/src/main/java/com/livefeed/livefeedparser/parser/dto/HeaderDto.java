package com.livefeed.livefeedparser.parser.dto;

public record HeaderDto(
        String innerHtml,
        String articleTitle,
        String publicationTime,
        String originArticleUrl,
        String journalistName
) {

    public static HeaderDto of(String innerHtml, String articleTitle,
                               String publicationTime, String originArticleUrl, String journalistName) {
        return new HeaderDto(innerHtml, articleTitle,
                publicationTime, originArticleUrl, journalistName);
    }
}
