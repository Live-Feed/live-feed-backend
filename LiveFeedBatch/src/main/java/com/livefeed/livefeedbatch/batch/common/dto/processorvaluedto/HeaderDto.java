package com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto;

public record HeaderDto(
        String html,
        String articleTitle,
        String pressCompanyName,
        String publicationTime,
        String originArticleUrl
) {

    public static HeaderDto of(String html, String articleTitle, String pressCompanyName,
                               String publicationTime, String originArticleUrl) {
        return new HeaderDto(html, articleTitle, pressCompanyName, publicationTime, originArticleUrl);
    }
}
