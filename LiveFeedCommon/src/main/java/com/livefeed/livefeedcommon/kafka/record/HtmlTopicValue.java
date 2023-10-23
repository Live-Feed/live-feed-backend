package com.livefeed.livefeedcommon.kafka.record;

public record HtmlTopicValue(
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
