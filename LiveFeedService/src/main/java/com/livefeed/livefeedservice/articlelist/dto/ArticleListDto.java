package com.livefeed.livefeedservice.articlelist.dto;

import java.util.List;

public record ArticleListDto(
        List<ArticleDto> articles,
        boolean isLast,
        Float lastScore,
        Long lastId,
        String pit
) {

    public static ArticleListDto of(List<ArticleDto> articles, boolean isLast, Float lastScore, Long lastId, String pit) {
        return new ArticleListDto(
                articles, isLast, lastScore, lastId, pit
        );
    }
}
