package com.livefeed.livefeedservice.articlelist.dto;

import java.util.List;

public record ArticleListDto(
        List<ArticleDto> articles,
        boolean isLast,
        Float lastScore,
        Long lastId,
        String pit
) {

    public static ArticleListDto from(SearchResultDto searchResultDto, int pageSize) {
        List<ArticleDto> articleList = searchResultDto.articleDtoList();

        return new ArticleListDto(
                articleList,
                articleList.size() < pageSize,
                articleList.size() > 0 ? articleList.get(articleList.size() - 1).score() : null,
                articleList.size() > 0 ? articleList.get(articleList.size() - 1).articleId() : null,
                searchResultDto.pit()
        );
    }
}
