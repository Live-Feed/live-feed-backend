package com.livefeed.livefeedservice.articlelist.dto;

import java.util.List;

public record ArticleListDto(
        List<ArticleDto> articles,
        boolean isLast,
        Long lastId,
        String pit
) {

    public static ArticleListDto of(List<ArticleDto> articles, boolean isLast, Long lastId, String pit) {
        return new ArticleListDto(
                articles, isLast, lastId, pit
        );
    }
}
