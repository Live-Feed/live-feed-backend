package com.livefeed.livefeedservice.articlelist.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SearchScoreQueryMaker extends QueryMaker {

    @Override
    public NativeQuery makeArticleListQuery(SearchQueryParam searchQueryParam) {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();

        if (isShouldSearchQuery(searchQueryParam)) {
            makeShouldQuery(nativeQueryBuilder, searchQueryParam.getType(), searchQueryParam.getKeywords());
        }

        makeHighlightQuery(nativeQueryBuilder, searchQueryParam.getType());
        makeQuerySize(nativeQueryBuilder, searchQueryParam.getSize());
        makeSearchAfterQuery(nativeQueryBuilder, searchQueryParam.getLastId());
        makePitQuery(nativeQueryBuilder, searchQueryParam.getPit());

        return nativeQueryBuilder.build();
    }

    @Override
    public boolean isRightQueryMaker(SearchQueryParam searchQueryParam) {
        return searchQueryParam.isRelatedQuery();
    }
}
