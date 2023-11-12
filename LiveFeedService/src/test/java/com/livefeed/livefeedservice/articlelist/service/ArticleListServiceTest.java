package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.common.util.SearchQueryParam;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@Disabled
@SpringBootTest
class ArticleListServiceTest {

    @Autowired
    private ArticleListService articleListService;

    @DisplayName("기사들을 제대로 가져오는지 확인하는 테스트")
    @Test
    void getArticleList() {
        // given
        SearchQueryParam searchQueryParam = SearchQueryParam.makeParam(List.of("articleTitle", "bodyHtml"), List.of("SSG"), 3, List.of("id-desc"), null, null);
        // when
        ArticleListDto articleList = articleListService.getArticleList(searchQueryParam);
        // then

    }
}