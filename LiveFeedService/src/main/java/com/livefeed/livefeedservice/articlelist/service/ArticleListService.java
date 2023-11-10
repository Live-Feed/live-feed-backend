package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.domain.SearchHitModifier;
import com.livefeed.livefeedservice.articlelist.dto.ArticleDto;
import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.articlelist.repository.NativeQueryElasticsearchOperations;
import com.livefeed.livefeedservice.common.util.SearchQueryParam;
import com.livefeed.livefeedservice.elasticsearch.entity.ElasticsearchArticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Explanation;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleListService {

    private final NativeQueryElasticsearchOperations nativeQueryElasticsearchRepository;
    private final SearchHitModifier searchHitModifier;

    public ArticleListDto getArticleList(SearchQueryParam searchQueryParam) {
        // pit가 설정이 안되어 있는 경우 pit를 먼저 가져온다.
        if (searchQueryParam.getPit() == null) {
            searchQueryParam.setInitialPit(nativeQueryElasticsearchRepository.getPit("articles"));
        }

        SearchHits<ElasticsearchArticle> searchResult = nativeQueryElasticsearchRepository.getSearchResult(searchQueryParam);
        String pit = searchResult.getPointInTimeId();


        // TODO: 11/7/23 서치 쿼리인지 아닌지에 따라 로직이 바뀌어야 한다.

        List<ArticleDto> articleList = searchResult.getSearchHits().stream().map(search -> {



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

            return new ArticleDto(
                    search.getContent().getId(),
                    articleTitle,
                    search.getContent().getPressCompanyName(),
                    articleBody,
                    articleImg,
                    search.getContent().getPublicationTime()
            );
        }).toList();

        return ArticleListDto.of(
                articleList,
                articleList.size() < searchQueryParam.getSize(),
                articleList.size() > 0 ? articleList.get(articleList.size() - 1).articleId() : null,
                pit
        );
    }

}
