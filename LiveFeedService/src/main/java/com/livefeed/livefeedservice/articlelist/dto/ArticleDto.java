package com.livefeed.livefeedservice.articlelist.dto;

public record ArticleDto(
        Long articleId,
        String title,
        String pressCompany,
        String content,
        String photo,
        String minutesAgo,
        Float score,
        int views
) {
}
