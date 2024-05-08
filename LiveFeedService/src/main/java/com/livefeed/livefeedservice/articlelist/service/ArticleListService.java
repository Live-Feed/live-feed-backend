package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.articlelist.dto.SearchResultDto;
import com.livefeed.livefeedservice.articlelist.repository.SearchOperations;
import com.livefeed.livefeedservice.articlelist.util.SearchQueryParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleListService {

    private final SearchOperations searchOperations;

    private static final String ARTICLE_INDEX_NAME = "articles";

    public ArticleListDto getArticleList(SearchQueryParam searchQueryParam) {
        // pit가 설정이 안되어 있는 경우 pit를 먼저 가져온다.
        if (isFirstSearchRequest(searchQueryParam)) {
            searchQueryParam.setFirstRequestPit(searchOperations.getPit(ARTICLE_INDEX_NAME));
        }

        // TODO: 12/10/23 엘라스틱서치 내부에서 에러가 발생하는 경우도 체크할 필요가 있음
        SearchResultDto searchResultDto = searchOperations.getSearchResultTemp(searchQueryParam);
        return ArticleListDto.from(searchResultDto, searchQueryParam.getSize());
    }

    private boolean isFirstSearchRequest(SearchQueryParam searchQueryParam) {
        return searchQueryParam.getPit() == null || searchQueryParam.getPit().isEmpty();
    }
}
