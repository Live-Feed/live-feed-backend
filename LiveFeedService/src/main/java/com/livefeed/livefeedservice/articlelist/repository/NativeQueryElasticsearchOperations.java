package com.livefeed.livefeedservice.articlelist.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.core.OpenPointInTimeResponse;
import com.livefeed.livefeedservice.articlelist.domain.SearchHitModifier;
import com.livefeed.livefeedservice.articlelist.dto.ArticleDto;
import com.livefeed.livefeedservice.articlelist.dto.ModifiedSearchResultDto;
import com.livefeed.livefeedservice.articlelist.dto.SearchResultDto;
import com.livefeed.livefeedservice.articlelist.util.QueryMaker;
import com.livefeed.livefeedservice.articlelist.util.SearchQueryParam;
import com.livefeed.livefeedservice.common.exception.UnableToGetPitException;
import com.livefeed.livefeedservice.elasticsearch.entity.ElasticsearchArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class NativeQueryElasticsearchOperations implements SearchOperations {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchClient elasticsearchClient;
    private final QueryMaker queryMaker;
    private final SearchHitModifier searchHitModifier;

    private final String PIT_EXIST_TIME = "5m";

    @Override
    public String getPit(String indexName) {
        try {
            OpenPointInTimeResponse pit = elasticsearchClient.openPointInTime(b -> b.index(indexName)
                    .keepAlive(Time.of(builder -> builder.time(PIT_EXIST_TIME))));
            return pit.id();
        } catch (Exception e) {
            throw new UnableToGetPitException("pit 값을 생성할 수 없습니다.", e);
        }
    }

    @Override
    public SearchResultDto getSearchResultTemp(SearchQueryParam searchQueryParam) {
        NativeQuery nativeQuery = queryMaker.makeArticleListQuery(searchQueryParam);
        SearchHits<ElasticsearchArticle> searchHits = elasticsearchOperations.search(nativeQuery, ElasticsearchArticle.class);

        List<ArticleDto> articleDtoList = makeArticleDtoList(searchHits);
        String pit = searchHits.getPointInTimeId();

        return new SearchResultDto(articleDtoList, pit);
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
