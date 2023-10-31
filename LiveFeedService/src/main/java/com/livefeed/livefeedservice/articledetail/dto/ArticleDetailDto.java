package com.livefeed.livefeedservice.articledetail.dto;

import com.livefeed.livefeedservice.rdb.entity.Article;

public record ArticleDetailDto(
        Long articleId,
        String title,
        String pressCompany,
        String reporter,
        String publicationTime,
        String articleUrl,
        String contentHeader,
        String contentBody
) {

    public static ArticleDetailDto from(Article article) {
        return new ArticleDetailDto(
                article.getId(),
                article.getTitle(),
                article.getPressCompany().getCompanyName(),
                article.getJournalistName(),
                article.getPublicationTime(),
                article.getOriginArticleUrl(),
                article.getContentHeader(),
                article.getContentBody()
        );
    }
}
