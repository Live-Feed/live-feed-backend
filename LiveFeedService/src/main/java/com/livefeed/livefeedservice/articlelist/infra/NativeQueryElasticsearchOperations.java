package com.livefeed.livefeedservice.articlelist.infra;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.core.OpenPointInTimeResponse;
import com.livefeed.livefeedservice.articlelist.dto.ArticleDto;
import com.livefeed.livefeedservice.articlelist.dto.ModifiedSearchResultDto;
import com.livefeed.livefeedservice.articlelist.dto.SearchResultDto;
import com.livefeed.livefeedservice.articlelist.domain.repository.SearchOperations;
import com.livefeed.livefeedservice.articlelist.util.SearchQueryParam;
import com.livefeed.livefeedservice.common.exception.UnableToGetPitException;
import com.livefeed.livefeedservice.elasticsearch.entity.ElasticsearchArticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NativeQueryElasticsearchOperations implements SearchOperations {

    private static final String PIT_EXIST_TIME = "5m";
    private static final long PIT_DURATION_TIME = 5;

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchClient elasticsearchClient;
    private final QueryMaker queryMaker;
    private final SearchHitModifier searchHitModifier;

    @Override
    public SearchResultDto getSearchResult(SearchQueryParam searchQueryParam) {
        // pit가 설정이 안되어 있는 경우 pit를 먼저 가져온다.
        SearchQueryParam copySearchQueryParam = setPit(searchQueryParam);

        NativeQuery nativeQuery = queryMaker.makeArticleListQuery(copySearchQueryParam);
        SearchHits<ElasticsearchArticle> searchHits = getSearchHits(nativeQuery);

        List<ArticleDto> articleDtoList = makeArticleDtoList(searchHits);
        String pit = searchHits.getPointInTimeId();

        return new SearchResultDto(articleDtoList, pit);
    }

    private SearchQueryParam setPit(SearchQueryParam searchQueryParam) {
        if (isFirstSearchRequest(searchQueryParam)) {
            return SearchQueryParam.copyAndSetPit(searchQueryParam, getNewPit());
        } else {
            return SearchQueryParam.copyAndSetPit(searchQueryParam, searchQueryParam.getPit());
        }
    }

    private boolean isFirstSearchRequest(SearchQueryParam searchQueryParam) {
        return searchQueryParam.getPit() == null || searchQueryParam.getPit().isEmpty();
    }

    private SearchHits<ElasticsearchArticle> getSearchHits(NativeQuery nativeQuery) {
        try {
            return elasticsearchOperations.search(nativeQuery, ElasticsearchArticle.class);
        } catch (Exception e) {
            // pit 가 만료되면 에러가 발생함
            log.warn("pit is expired");
            nativeQuery.setPointInTime(new Query.PointInTime(getNewPit(), Duration.ofMinutes(PIT_DURATION_TIME)));
            return elasticsearchOperations.search(nativeQuery, ElasticsearchArticle.class);
        }
    }

    private String getNewPit() {
        try {
            OpenPointInTimeResponse pit = elasticsearchClient.openPointInTime(b -> b.index("articles")
                    .keepAlive(Time.of(builder -> builder.time(PIT_EXIST_TIME))));
            return pit.id();
        } catch (Exception e) {
            throw new UnableToGetPitException("pit 값을 생성할 수 없습니다.", e);
        }
    }

    private List<ArticleDto> makeArticleDtoList(SearchHits<ElasticsearchArticle> searchResult) {
        return searchResult.getSearchHits().stream().map(search -> {
            ModifiedSearchResultDto modifiedArticle = extractSearchResult(search);
            return new ArticleDto(
                    search.getContent().getId(),
                    modifiedArticle.articleTitle(),
                    search.getContent().getPressCompanyName(),
                    modifiedArticle.articleBody(),
                    modifiedArticle.articleImg(),
                    search.getContent().getPublicationTime(),
                    search.getScore()
            );
        }).toList();
    }

    private ModifiedSearchResultDto extractSearchResult(SearchHit<ElasticsearchArticle> search) {
        String articleTitle;
        List<String> headerHtmlHighLights = search.getHighlightField("articleTitle");
        if (headerHtmlHighLights.size() == 0) {
            articleTitle = search.getContent().getArticleTitle();
        } else {
            articleTitle = headerHtmlHighLights.get(0);
        }

        String articleBody;
        List<String> bodyHtmlHighLights = search.getHighlightField("bodyHtml");
        if (bodyHtmlHighLights.size() == 0) {
            articleBody = searchHitModifier.extractTextFromHtml(search.getContent().getBodyHtml());
        } else {
            articleBody = searchHitModifier.extractTagExceptBoldTag(bodyHtmlHighLights.get(0));
        }

        String articleImg = searchHitModifier.extractFirstImgFromHtml(search.getContent().getBodyHtml());
        return new ModifiedSearchResultDto(articleTitle, articleImg, articleBody);
    }
}
