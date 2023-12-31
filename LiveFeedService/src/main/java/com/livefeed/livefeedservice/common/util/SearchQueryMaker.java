package com.livefeed.livefeedservice.common.util;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.elasticsearch.core.query.Query.*;

@Slf4j
@Component
public class SearchQueryMaker {

    private final int PIT_DURATION_MINUTES = 3;

    public NativeQuery makeArticleListQuery(SearchQueryParam searchQueryParam) {

        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();

        if (isShouldSearchQuery(searchQueryParam)) {
            makeShouldQuery(nativeQueryBuilder, searchQueryParam.getType(), searchQueryParam.getKeywords());
            makeExplainQuery(nativeQueryBuilder);
        }

        makeQuerySize(nativeQueryBuilder, searchQueryParam.getSize());
        makeSortQuery(nativeQueryBuilder, searchQueryParam.getSort());
        makeSearchAfterQuery(nativeQueryBuilder, searchQueryParam.getLastId());
        makePitQuery(nativeQueryBuilder, searchQueryParam.getPit());

        return nativeQueryBuilder.build();
    }

    private boolean isShouldSearchQuery(SearchQueryParam searchQueryParam) {
        return searchQueryParam.getType() != null;
    }


    private void makeShouldQuery(NativeQueryBuilder nativeQueryBuilder, List<String> types, List<String> keywords) {
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

    private void makeQuerySize(NativeQueryBuilder nativeQueryBuilder, int size) {
        nativeQueryBuilder.withMaxResults(size);
    }

    private void makeSortQuery(NativeQueryBuilder nativeQueryBuilder, Sort sort) {
        nativeQueryBuilder.withSort(sort);
    }

    private void makeSearchAfterQuery(NativeQueryBuilder nativeQueryBuilder, Long lastId) {
        if (lastId != null) {
            nativeQueryBuilder.withSearchAfter(List.of(lastId));
        }
    }

    private void makePitQuery(NativeQueryBuilder nativeQueryBuilder, String pit) {
        if (pit != null) {
            nativeQueryBuilder.withPointInTime(new PointInTime(pit, Duration.ofMinutes(PIT_DURATION_MINUTES)));
        }
    }

    private void makeExplainQuery(NativeQueryBuilder nativeQueryBuilder) {
        nativeQueryBuilder.withExplain(true);
    }
}
