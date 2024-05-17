package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.articlelist.dto.KeywordEvent;
import com.livefeed.livefeedservice.articlelist.dto.SearchResultDto;
import com.livefeed.livefeedservice.articlelist.repository.SearchOperations;
import com.livefeed.livefeedservice.articlelist.util.SearchQueryParam;
import com.livefeed.livefeedservice.newarticle.repository.UserKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleListService {

    private final SearchOperations searchOperations;
    private final UserKeywordRepository userKeywordRepository;
    private final KeywordEventPublisher eventPublisher;

    public ArticleListDto getArticleList(SearchQueryParam searchQueryParam, String sseKey) {
        // TODO: 12/10/23 엘라스틱서치 내부에서 에러가 발생하는 경우도 체크할 필요가 있음
        SearchResultDto searchResultDto = searchOperations.getSearchResult(searchQueryParam);

        // 사용자 검색 결과를 redis에 저장해야함
        int updateCount = userKeywordRepository.updateUserKeywords(sseKey, searchQueryParam.getKeywords());
        log.info("update keyword count = {}", updateCount);

        if (updateCount != 0) {
            eventPublisher.publishKeywordEvent(new KeywordEvent(searchQueryParam.getKeywords()));
        }

        return ArticleListDto.from(searchResultDto, searchQueryParam.getSize());
    }
}
