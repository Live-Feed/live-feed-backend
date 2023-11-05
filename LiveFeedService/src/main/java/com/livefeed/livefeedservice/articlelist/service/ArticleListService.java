package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.dto.ArticleDto;
import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.articlelist.repository.NativeQueryElasticsearchOperations;
import com.livefeed.livefeedservice.common.util.SearchQueryParam;
import com.livefeed.livefeedservice.elasticsearch.entity.ElasticsearchArticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleListService {

    private final NativeQueryElasticsearchOperations<ElasticsearchArticle> nativeQueryElasticsearchRepository;

    public ArticleListDto getArticleList(SearchQueryParam searchQueryParam) {

        // pit가 설정이 안되어 있는 경우 pit를 먼저 가져온다.
        if (searchQueryParam.getPit() == null) {
            searchQueryParam.setInitialPit(nativeQueryElasticsearchRepository.getPit("articles"));
        }

        SearchHits<ElasticsearchArticle> searchResult = nativeQueryElasticsearchRepository.getSearchResult(searchQueryParam);

//        searchResult.stream().;

        String pit = searchResult.getPointInTimeId();

        for (SearchHit<ElasticsearchArticle> searchHit : searchResult) {

        }

        List<ArticleDto> articleList = searchResult.getSearchHits().stream().map(search -> new ArticleDto(
                search.getContent().getId(),
                search.getContent().getArticleTitle(),
                search.getContent().getPressCompanyName(),
                search.getContent().getHeaderHtml() + search.getContent().getBodyHtml(),
                search.getContent().getHeaderHtml(),
                search.getContent().getPublicationTime()
        )).toList();




        return null;
    }

}
