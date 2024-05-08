package com.livefeed.livefeedservice.articlelist.util;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class QueryMaker {

    private final int PIT_DURATION_MINUTES = 3;

    public NativeQuery makeArticleListQuery(SearchQueryParam searchQueryParam) {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();

        if (isShouldSearchQuery(searchQueryParam)) {
            makeShouldQuery(nativeQueryBuilder, searchQueryParam.getType(), searchQueryParam.getKeywords());
            makeHighlightQuery(nativeQueryBuilder, searchQueryParam.getType());
        }

        makeQuerySize(nativeQueryBuilder, searchQueryParam.getSize());
        makeSortQuery(nativeQueryBuilder, searchQueryParam.getSort());
        makeSearchAfterQuery(nativeQueryBuilder, searchQueryParam.getLastScore(), searchQueryParam.getLastId());
        makePitQuery(nativeQueryBuilder, searchQueryParam.getPit());

        return nativeQueryBuilder.build();
    }

    protected boolean isShouldSearchQuery(SearchQueryParam searchQueryParam) {
        return searchQueryParam.getType() != null;
    }

    protected void makeShouldQuery(NativeQueryBuilder nativeQueryBuilder, List<String> types, List<String> keywords) {
        List<Query> queryList = new ArrayList<>();

        if (keywords == null) {
            return;
        }

        for (String type : types) {
            for (String keyword : keywords) {
                Query query = Query.of(q -> q.match(t -> t.field(type).query(keyword)));
                queryList.add(query);
            }
        }
        nativeQueryBuilder.withQuery(Query.of(q -> q.bool(b -> b.should(queryList))));
    }

    protected void makeHighlightQuery(NativeQueryBuilder nativeQueryBuilder, List<String> types) {
        HighlightParameters highlightParameters = HighlightParameters.builder()
                .withNumberOfFragments(1)
                .withFragmentSize(500)
                .withPreTags("<b>")
                .withPostTags("</b>")
                .build();

        List<HighlightField> highlightFields = types.stream().map(HighlightField::new).toList();

        nativeQueryBuilder.withHighlightQuery(new HighlightQuery(new Highlight(highlightParameters, highlightFields), String.class));
    }

    protected void makeQuerySize(NativeQueryBuilder nativeQueryBuilder, int size) {
        nativeQueryBuilder.withMaxResults(size);
    }

    protected void makeSortQuery(NativeQueryBuilder nativeQueryBuilder, Sort sort) {
        nativeQueryBuilder.withSort(sort);
    }

    protected void makeSearchAfterQuery(NativeQueryBuilder nativeQueryBuilder, Double lastScore, Long lastId) {
        List<Object> searchAfterList = new ArrayList<>();

        if (lastScore != null) {
            searchAfterList.add(lastScore);
        }

        if (lastId != null) {
            searchAfterList.add(lastId);
        }

        nativeQueryBuilder.withSearchAfter(searchAfterList);
    }

    protected void makePitQuery(NativeQueryBuilder nativeQueryBuilder, String pit) {
        if (pit != null) {
            nativeQueryBuilder.withPointInTime(new org.springframework.data.elasticsearch.core.query.Query.PointInTime(pit, Duration.ofMinutes(PIT_DURATION_MINUTES)));
        }
    }
}
