package com.livefeed.livefeedsaver.kafka.consumer.dto;

public record ConsumerValueDto(
        String articleTitle,
        String publicationTime,
        String pressCompanyName,
        String journalistName,
        String journalistEmail,
        String originArticleUrl,
        String headerHtml,
        String bodyHtml
) {
}
