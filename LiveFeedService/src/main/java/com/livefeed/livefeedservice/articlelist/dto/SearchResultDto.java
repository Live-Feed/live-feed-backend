package com.livefeed.livefeedservice.articlelist.dto;

import java.util.List;

public record SearchResultDto(
        List<ArticleDto> articleDtoList,
        String pit
) {
}
