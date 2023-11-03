package com.livefeed.livefeedservice.articlelist.dto;

import java.util.List;

public record ArticleListDto(
        List<ArticleDto> articles,
        boolean isLast,
        long lastId,
        String pit
) {
}
