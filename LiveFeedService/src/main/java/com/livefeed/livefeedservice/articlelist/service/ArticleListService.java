package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.common.util.SearchQueryMaker;
import com.livefeed.livefeedservice.common.util.SearchQueryParam;
import com.livefeed.livefeedservice.elasticsearch.entity.ElasticsearchArticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleListService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final SearchQueryMaker searchQueryMaker;

    public ArticleListDto getArticleList(SearchQueryParam searchQueryParam) {

        // pit가 설정이 안되어 있는 경우 pit를 먼저 가져온다.

        NativeQuery nativeQuery = searchQueryMaker.makeArticleListQuery(searchQueryParam);
        SearchHits<ElasticsearchArticle> searchHits = elasticsearchOperations.search(nativeQuery, ElasticsearchArticle.class);


        return null;
    }

}
