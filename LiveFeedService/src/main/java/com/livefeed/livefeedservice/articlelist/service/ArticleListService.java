package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.domain.SearchHitModifier;
import com.livefeed.livefeedservice.articlelist.dto.ArticleDto;
import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.articlelist.dto.ModifiedSearchResultDto;
import com.livefeed.livefeedservice.articlelist.repository.NativeQueryElasticsearchOperations;
import com.livefeed.livefeedservice.common.util.SearchQueryParam;
import com.livefeed.livefeedservice.elasticsearch.entity.ElasticsearchArticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Explanation;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleListService {

    private final NativeQueryElasticsearchOperations nativeQueryElasticsearchRepository;
    private final SearchHitModifier searchHitModifier;

    private static final String ARTICLE_INDEX_NAME = "articles";

    public ArticleListDto getArticleList(SearchQueryParam searchQueryParam) {
        // pit가 설정이 안되어 있는 경우 pit를 먼저 가져온다.
        if (isFirstSearchRequest(searchQueryParam)) {
            searchQueryParam.setFirstRequestPit(nativeQueryElasticsearchRepository.getPit(ARTICLE_INDEX_NAME));
        }

        // TODO: 12/10/23 엘라스틱서치 내부에서 에러가 발생하는 경우도 체크할 필요가 있음
        SearchHits<ElasticsearchArticle> searchResult = nativeQueryElasticsearchRepository.getSearchResult(searchQueryParam);
        String pit = searchResult.getPointInTimeId();

        List<ArticleDto> articleList = makeArticleDtoList(searchResult);

        return ArticleListDto.of(
                articleList,
                articleList.size() < searchQueryParam.getSize(),
                articleList.size() > 0 ? articleList.get(articleList.size() - 1).articleId() : null,
                pit
        );
    }

    private boolean isFirstSearchRequest(SearchQueryParam searchQueryParam) {
        return searchQueryParam.getPit() == null || searchQueryParam.getPit().isEmpty();
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
                    search.getContent().getPublicationTime()
            );
        }).toList();
    }

    private ModifiedSearchResultDto extractSearchResult(SearchHit<ElasticsearchArticle> search) {
        String articleTitle = searchHitModifier.extractTextFromHtml(search.getContent().getArticleTitle());
        String articleImg = searchHitModifier.extractFirstImgFromHtml(search.getContent().getBodyHtml());
        String articleBody = searchHitModifier.extractTextFromHtml(search.getContent().getBodyHtml());

        Explanation explanation = search.getExplanation();

        if (explanation != null) {
            String description = explanation.getDescription().startsWith("weight")
                    ? explanation.getDescription()
                    : explanation.getDetails().get(0).getDescription();

            String searchedWord = searchHitModifier.extractSearchedWord(description);

            articleTitle = searchHitModifier.boldingWord(searchedWord, articleTitle);
            articleBody = searchHitModifier.boldingWord(searchedWord, articleBody);
        }

        articleBody = searchHitModifier.cutTextFromBodyText(articleBody);

        return new ModifiedSearchResultDto(articleTitle, articleImg, articleBody);
    }

}
