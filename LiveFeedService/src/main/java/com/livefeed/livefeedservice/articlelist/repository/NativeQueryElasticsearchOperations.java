package com.livefeed.livefeedservice.articlelist.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.core.OpenPointInTimeResponse;
import com.livefeed.livefeedservice.common.exception.UnableToGetPitException;
import com.livefeed.livefeedservice.common.util.SearchQueryMaker;
import com.livefeed.livefeedservice.common.util.SearchQueryParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class NativeQueryElasticsearchOperations<T> implements SearchOperations<T> {

    private T t;

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchClient elasticsearchClient;
    private final SearchQueryMaker searchQueryMaker;

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
    public SearchHits<T> getSearchResult(SearchQueryParam searchQueryParam) {
        NativeQuery nativeQuery = searchQueryMaker.makeArticleListQuery(searchQueryParam);
        return elasticsearchOperations.search(nativeQuery, (Class<T>) t.getClass());
    }
}
